package test.org.evan.libraries.rocketmq.support.consumer;

/**
 * @author Evan.Shen
 * @since 2019-08-19
 */


import com.alibaba.fastjson.JSON;
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
@RocketMQMessageListener(topic = "TEST_0_TOPIC", consumerGroup = "consumer0Group", consumeMode = ConsumeMode.CONCURRENTLY, messageModel = MessageModel.BROADCASTING)
public class ConsumerTopicTest implements RocketMQListener<Demo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerTopicTest.class);

    public void onMessage(Demo message) {
        LOGGER.info("received message: {}", JSON.toJSON(message));
        // throw new IllegalStateException();
    }
}

