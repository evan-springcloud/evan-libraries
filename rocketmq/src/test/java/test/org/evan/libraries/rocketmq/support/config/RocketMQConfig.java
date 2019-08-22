package test.org.evan.libraries.rocketmq.support.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.org.evan.libraries.rocketmq.support.consumer.ConsumerTopicTest;

import java.util.List;

/**
 * @author Evan.Shen
 * @since 2019-08-18
 */
@Configuration
@ConditionalOnClass({DefaultMQProducer.class})
@EnableConfigurationProperties({RocketMQProperties.class})
public class RocketMQConfig {

    @Bean(destroyMethod = "shutdown")
    public DefaultMQProducer rocketMQProducer(RocketMQProperties rocketMQProperties) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup");
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        //producer.start();
        return producer;
    }

//    @Bean(destroyMethod = "shutdown")
//    public DefaultMQPushConsumer consumer0(RocketMQProperties rocketMQProperties) throws MQClientException {
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer0Group");
//        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
//        consumer.subscribe("TEST_0_TOPIC", "*");
//
//        // 开启内部类实现监听
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//            private final Logger LOGGER = LoggerFactory.getLogger(MessageListenerConcurrently.class);
//
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                for (MessageExt e: msgs){
//                    LOGGER.info(e + "");
//                }
//
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
//
//        consumer.start();
//
//        return consumer;
//    }
}
