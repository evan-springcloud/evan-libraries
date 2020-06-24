package test.org.evan.libraries.kafka.consumer;

/**
 * @author Evan.Shen
 * @since 2019-08-19
 */


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @Autowired
    private ConsumerDataStater consumerDataStat;

    @KafkaListener(topics = {"test_topic_0"})
    public void listen1(ConsumerRecord<?, ?> record) {
        consumerDataStat.stat(record);
    }

    @KafkaListener(topics = {"test_topic_1"})
    public void listen2(ConsumerRecord<?, ?> record) {
        consumerDataStat.stat(record);
    }

//    @KafkaListener(topics = {"test_topic_2"})
//    public void listen3(ConsumerRecord<?, ?> record) {
//        consumerDataStat.stat(record);
//    }
}


