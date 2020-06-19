package test.org.evan.libraries.kafka.support.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
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
    private BlockingQueue<ConsumerRecord<?, ?>> consumerDataCountTmpStore = new LinkedBlockingDeque<>(1024);
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

        statDataPrintThread = new Thread(new StatDataPrinter(consumerDataStatResult, duplicateCheckResult));
        statDataPrintThread.start();

        statThread = new Thread(new StatRunnable(consumerDataCountTmpStore, consumerDataStatResult));
        statThread.start();
    }

    @PreDestroy
    public void destroy() {
        statThread.interrupt();
        statDataPrintThread.interrupt();
    }

    public void stat(ConsumerRecord<?, ?> record) {
        String key = record.key() + "";

        try {
            Thread.sleep(1); //模拟处理需要的时间
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
        }

        Map<String, Object> map = new HashMap();
        map.put("topic", record.topic());
        map.put("partition", record.partition());
        map.put("key", key);
        map.put("value", record.value());
        map.put("offset",record.offset());

        listOperations.rightPush("receive_total", map);
        listOperations.rightPush("receive_" + record.topic(), map);

//        if (hashOperations.hasKey("total_no_duplicate", record.key())) {
//            operationsDuplicate.rightPush("duplicate", map);
//        } else {
//            hashOperations.put("total_no_duplicate", record.key() + "", map);
//            //hashOperations.put("total_no_duplicate_byid", demo.getId() + "", map);
//        }

        //hashOperations.put(key, record.key() + "", map);
        try {
            consumerDataCountTmpStore.put(record);
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
        }

    }
}

@Slf4j
class StatRunnable implements Runnable {
    private BlockingQueue<ConsumerRecord<?, ?>> consumerDataCountTmpStore;
    private ConcurrentHashMap<String, Integer> consumerDataStatResult;


    public StatRunnable(BlockingQueue<ConsumerRecord<?, ?>> consumerDataCountTmpStore, ConcurrentHashMap<String, Integer> consumerDataStatResult) {
        this.consumerDataCountTmpStore = consumerDataCountTmpStore;
        this.consumerDataStatResult = consumerDataStatResult;
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecord<?, ?> o = null;

            try {
                o = consumerDataCountTmpStore.take();
            } catch (InterruptedException e) {
//                log.warn(e.getMessage(), e);
            }

            if (o != null) {
                stat(o.topic());
                // stat(o.topic() + "__" + o.partition());
                consumerDataCountTmpStore.remove(o);
            }
        }
    }

    private void stat(String key) {
        Integer count = consumerDataStatResult.get(key);
        if (count == null) {
            count = 0;
        }
        count++;
        consumerDataStatResult.put(key, count);
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
            }
        }

        log.info("");
    }
}

//
//@Slf4j
//class DuplicateChecker implements Runnable {
//    private RedisTemplate redisTemplate;
//    private ConcurrentHashMap<String, Integer> duplicateCheckResult;
//
//    private Set<String> duplicateCheckSet = new HashSet<>(10240);
//
//    private BoundHashOperations<String, String, Demo> boundHashOperations;
//
//    public DuplicateChecker(RedisTemplate redisTemplate, ConcurrentHashMap<String, Integer> duplicateCheckResult) {
//        this.redisTemplate = redisTemplate;
//        this.duplicateCheckResult = duplicateCheckResult;
//
//        boundHashOperations = redisTemplate.boundHashOps("rocketMsg");
//    }
//
//    @Override
//    public void run() {
//        while (true) {
////            MessageExt o = null;
////
////            //boundHashOperations.entries();
////
//////            try {
//////                o = consumerDataTmpStore.take();
//////            } catch (InterruptedException e) {
//////                log.warn(e.getMessage(), e);
//////            }
////
////            if (o != null) {
////                Demo demo = JSON.parseObject(o.getBody(), Demo.class);
////
////                String key = demo.getId() + "";
////
////                if (duplicateCheckSet.contains(key)) {
////                    duplicateCheckResult.put(key, 2);
////                } else {
////                    duplicateCheckSet.add(key);
////                }
////            }
//        }
//    }
//}

