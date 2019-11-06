package test.org.evan.libraries.kafka.support.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
public class ProducerDataStater {
    //private BlockingQueue<MessageExt> consumerDataTmpStore = new LinkedBlockingDeque<>(5120);
    private BlockingQueue<String> producerSuccessStore = new LinkedBlockingDeque<>(2048);
    private ConcurrentHashMap<String, Integer> producerSuccessStatResult = new ConcurrentHashMap<>();

    private BlockingQueue<String> producerFailStore = new LinkedBlockingDeque<>(256);
    private ConcurrentHashMap<String, Integer> producerFailStatResult = new ConcurrentHashMap<>();

    private Thread statSuccessThread;
    private Thread statFailThread;
    private Thread statDataPrintThread;

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

        statDataPrintThread = new Thread(new StatDataPrinter(producerSuccessStatResult, producerFailStatResult));
        statDataPrintThread.start();

        statSuccessThread = new Thread(new StatRunnable(producerSuccessStore, producerSuccessStatResult));
        statSuccessThread.start();

        statFailThread = new Thread(new StatRunnable(producerFailStore, producerFailStatResult));
        statFailThread.start();
    }

    @PreDestroy
    public void destroy() {
        statSuccessThread.interrupt();
        statFailThread.interrupt();
        statDataPrintThread.interrupt();
    }

    public void statSuccess(RecordMetadata recordMetadata) {
        try {
            producerSuccessStore.put(recordMetadata.topic());
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
        }
    }

    public void statFail(String topic) {
        try {
            producerFailStore.put(topic);
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
        }
    }
}

@Slf4j
class StatRunnable implements Runnable {
    private BlockingQueue<String> producerStore;
    private ConcurrentHashMap<String, Integer> producerStatResult;


    public StatRunnable(BlockingQueue<String> producerStore, ConcurrentHashMap<String, Integer> producerStatResult) {
        this.producerStore = producerStore;
        this.producerStatResult = producerStatResult;
    }

    @Override
    public void run() {
        while (true) {
            String topic = null;

            try {
                topic = producerStore.take();
            } catch (InterruptedException e) {
//                log.warn(e.getMessage(), e);
            }

            if (topic != null) {
                stat(topic);
                producerStore.remove(topic);
            }
        }
    }

    private void stat(String key) {
        Integer count = producerStatResult.get(key);
        if (count == null) {
            count = 0;
        }
        count++;
        producerStatResult.put(key, count);
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
//    private ConcurrentHashMap<String, Integer> producerFailStatResult;
//
//    private Set<String> duplicateCheckSet = new HashSet<>(10240);
//
//    private BoundHashOperations<String, String, Demo> boundHashOperations;
//
//    public DuplicateChecker(RedisTemplate redisTemplate, ConcurrentHashMap<String, Integer> producerFailStatResult) {
//        this.redisTemplate = redisTemplate;
//        this.producerFailStatResult = producerFailStatResult;
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
////                    producerFailStatResult.put(key, 2);
////                } else {
////                    duplicateCheckSet.add(key);
////                }
////            }
//        }
//    }
//}

