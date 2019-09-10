package test.org.evan.libraries.rocketmq.support.consumer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import test.org.evan.libraries.rocketmq.support.model.Demo;
import test.org.evan.libraries.rocketmq.support.model.MessageStatBO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Evan.Shen
 * @since 2019-08-31
 */
@Component
@Slf4j
public class ConsumerDataStater {
    //private BlockingQueue<MessageExt> consumerDataTmpStore = new LinkedBlockingDeque<>(5120);
    private BlockingQueue<MessageStatBO> consumerDataCountTmpStore = new LinkedBlockingDeque<>(1024);
    private ConcurrentHashMap<String, Integer> consumerDataStatResult = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> duplicateCheckResult = new ConcurrentHashMap<>(128);

    private Thread statThread;
    private Thread statDataPrintThread;
    private Thread duplicateCheckThread;

    @Autowired
    private RedisTemplate redisTemplate;

    private ListOperations<String, Map<String, Object>> listOperations;

    private HashOperations<String, String, Map<String, Object>> hashOperations;

    private ListOperations<String, Map<String, Object>> operationsDuplicate;

    @PostConstruct
    public void init() {
        listOperations = redisTemplate.opsForList();
        hashOperations = redisTemplate.opsForHash();
        operationsDuplicate = redisTemplate.opsForList();
//        boundListOperationsForAll = redisTemplate.boundListOps("all");
//        BoundListOperations = redisTemplate.boundListOps("rocketMsg");
//        boundHashOperations = redisTemplate.boundHashOps("rocketMsg");
//        boundHashOperations = redisTemplate.boundHashOps("rocketMsg");


        statThread = new Thread(new StatRunnable(consumerDataCountTmpStore, consumerDataStatResult));
        //duplicateCheckThread = new Thread(new DuplicateChecker(redisTemplate, duplicateCheckResult));
        statDataPrintThread = new Thread(new StatDataPrinter(consumerDataStatResult, duplicateCheckResult));

        statThread.start();
        //duplicateCheckThread.start();
        statDataPrintThread.start();
    }

    @PreDestroy
    public void destroy() {
        statThread.interrupt();
        //duplicateCheckThread.interrupt();
        statDataPrintThread.interrupt();
    }


    public void stat(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        MessageQueue messageQueue = context.getMessageQueue();
        MessageStatBO messageStatBO = new MessageStatBO(messageQueue.getBrokerName(), messageQueue.getTopic(), msgs.size());

        try {
            consumerDataCountTmpStore.put(messageStatBO);

            for (MessageExt messageExt : msgs) {
                SocketAddress socketAddress = messageExt.getStoreHost();

                String key = messageQueue.getBrokerName() + "-" + messageQueue.getTopic() + "-" + socketAddress;

                Demo demo = JSON.parseObject(messageExt.getBody(), Demo.class);

                Map<String, Object> map = new HashMap();

                map.put("broker", messageQueue.getBrokerName());
                map.put("topic", messageQueue.getTopic());
                map.put("message", messageExt);

                listOperations.rightPush("total", map);

                if (hashOperations.hasKey("total_no_duplicate", messageExt.getMsgId())) {
                    operationsDuplicate.rightPush("duplicate", map);
                } else {
                    hashOperations.put("total_no_duplicate", messageExt.getMsgId(), map);
                    hashOperations.put("total_no_duplicate_byid", demo.getId() + "", map);
                }

                hashOperations.put(key, messageExt.getMsgId(), map);
                //consumerDataTmpStore.put(messageExt);
            }

        } catch (InterruptedException ex) {
            log.warn(ex.getMessage(), ex);
        }
    }
}

@Slf4j
class StatRunnable implements Runnable {

    private BlockingQueue<MessageStatBO> consumerDataCountTmpStore;
    private ConcurrentHashMap<String, Integer> consumerDataStatResult;


    public StatRunnable(BlockingQueue<MessageStatBO> consumerDataCountTmpStore, ConcurrentHashMap<String, Integer> consumerDataStatResult) {
        this.consumerDataCountTmpStore = consumerDataCountTmpStore;
        this.consumerDataStatResult = consumerDataStatResult;
    }

    @Override
    public void run() {
        while (true) {
            MessageStatBO o = null;

            try {
                o = consumerDataCountTmpStore.take();
            } catch (InterruptedException e) {
                log.warn(e.getMessage(), e);
            }

            if (o != null) {
                String key = o.getBrokerName() + "__" + o.getTopicName();

                Integer count = consumerDataStatResult.get(key);
                if (count == null) {
                    count = 0;
                }
                count += o.getCount();
                consumerDataStatResult.put(key, count);

                consumerDataCountTmpStore.remove(o);
            }
        }
    }
}

@Slf4j
class DuplicateChecker implements Runnable {
    private RedisTemplate redisTemplate;
    private ConcurrentHashMap<String, Integer> duplicateCheckResult;

    private Set<String> duplicateCheckSet = new HashSet<>(10240);

    private BoundHashOperations<String, String, Demo> boundHashOperations;

    public DuplicateChecker(RedisTemplate redisTemplate, ConcurrentHashMap<String, Integer> duplicateCheckResult) {
        this.redisTemplate = redisTemplate;
        this.duplicateCheckResult = duplicateCheckResult;

        boundHashOperations = redisTemplate.boundHashOps("rocketMsg");
    }

    @Override
    public void run() {
        while (true) {
            MessageExt o = null;

            //boundHashOperations.entries();

//            try {
//                o = consumerDataTmpStore.take();
//            } catch (InterruptedException e) {
//                log.warn(e.getMessage(), e);
//            }

            if (o != null) {
                Demo demo = JSON.parseObject(o.getBody(), Demo.class);

                String key = demo.getId() + "";

                if (duplicateCheckSet.contains(key)) {
                    duplicateCheckResult.put(key, 2);
                } else {
                    duplicateCheckSet.add(key);
                }
            }
        }
    }
}

@Slf4j
class StatDataPrinter implements Runnable {
    private ConcurrentHashMap<String, Integer> consumerDataStatResult;
    private ConcurrentHashMap<String, Integer> duplicateCheckResult;

    public StatDataPrinter(ConcurrentHashMap<String, Integer> consumerDataStatResult, ConcurrentHashMap<String, Integer> duplicateCheckResult) {
        this.consumerDataStatResult = consumerDataStatResult;
        this.duplicateCheckResult = duplicateCheckResult;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                printData();
            }
            printData();
        }
    }

    private void printData() {
        log.info("print data:");

        if (consumerDataStatResult.isEmpty()) {
            log.info(">>>> no data");
        } else {
            for (Map.Entry<String, Integer> entry : consumerDataStatResult.entrySet()) {
                log.info(">>>> {}: {}", entry.getKey(), entry.getValue());
            }

            if (!duplicateCheckResult.isEmpty()) {
                log.info(">>>> duplicate data count {}", duplicateCheckResult.size());
//                for (Map.Entry<String, Integer> entry : duplicateCheckResult.entrySet()) {
//
//                }
            }
        }

        log.info("");
    }
}