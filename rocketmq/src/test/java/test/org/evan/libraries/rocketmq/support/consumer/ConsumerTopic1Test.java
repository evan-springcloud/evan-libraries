package test.org.evan.libraries.rocketmq.support.consumer;

/**
 * @author Evan.Shen
 * @since 2019-08-19
 */


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import test.org.evan.libraries.rocketmq.support.model.Demo;

@Slf4j
@Service
@RocketMQMessageListener(topic = "TEST_2_TOPIC", consumerGroup = "group1", consumeMode = ConsumeMode.CONCURRENTLY, messageModel = MessageModel.CLUSTERING)
public class ConsumerTopic2Test implements RocketMQListener<Demo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTopic2Test.class);

    public void onMessage(Demo message) {
        LOGGER.info("received message: {}", message);
        // throw new IllegalStateException();
    }
}

