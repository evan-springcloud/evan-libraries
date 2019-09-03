package test.org.evan.libraries.rocketmq.support.consumer;

/**
 * @author Evan.Shen
 * @since 2019-08-19
 */


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.evan.libraries.rocketmq.ConsumerConfig;
import org.evan.libraries.rocketmq.ConsumerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.org.evan.libraries.rocketmq.support.config.ServerConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Slf4j
@Component
//@RocketMQMessageListener(topic = "TEST_0_TOPIC", consumerGroup = "consumer0Group", consumeMode = ConsumeMode.ORDERLY, messageModel = MessageModel.CLUSTERING)
//public class ConsumerTopicTest implements RocketMQListener<Demo> {
public class ConsumerTopic0Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTopic0Test.class);

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
        consumerConfig.setGroup("consumer0Group");
        consumerConfig.setTopic("TEST_0_TOPIC");

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


