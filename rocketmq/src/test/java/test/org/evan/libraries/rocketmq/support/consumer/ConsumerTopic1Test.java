package test.org.evan.libraries.rocketmq.support.consumer;

/**
 * @author Evan.Shen
 * @since 2019-08-19
 */


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.evan.libraries.rocketmq.ConsumerConfig;
import org.evan.libraries.rocketmq.ConsumerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.org.evan.libraries.rocketmq.support.config.ServerConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
//@RocketMQMessageListener(topic = "TEST_1_TOPIC", consumerGroup = "consumer1Group", consumeMode = ConsumeMode.CONCURRENTLY, messageModel = MessageModel.CLUSTERING)
public class ConsumerTopic1Test {
    @Autowired
    private RocketMQProperties rocketMQProperties;

    private DefaultMQPushConsumer consumer;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ConsumerDataStater consumerDataStat;

    @PostConstruct
    public void init() throws MQClientException {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setNameServer(rocketMQProperties.getNameServer());
        consumerConfig.setGroup("consumer1Group");
        consumerConfig.setTopic("TEST_1_TOPIC");

        consumer = ConsumerFactory.create(consumerConfig, new ListenterForDemo(consumerDataStat));
        consumer.setConsumeThreadMin(2);
        consumer.setConsumeThreadMax(4);
        consumer.setPullBatchSize(4);
        consumer.start();
    }

    @PreDestroy
    public void shutdown() {
        consumer.shutdown();
    }
}


