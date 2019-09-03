package org.evan.libraries.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.evan.libraries.utils.RandomDataUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Evan.Shen
 * @since 2019-08-24
 */
@Slf4j
public class ConsumerFactory {

    public static DefaultMQPushConsumer create(String nameServer,
                                               String group,
                                               String topic,
                                               MessageListenerConcurrently messageListenerConcurrently) throws MQClientException {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setGroup(group);
        consumerConfig.setNameServer(nameServer);
        consumerConfig.setTopic(topic);

        return ConsumerFactory.create(consumerConfig, messageListenerConcurrently);
    }


    public static DefaultMQPushConsumer create(ConsumerConfig consumerConfig,
                                               MessageListenerConcurrently messageListenerConcurrently)
            throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerConfig.getGroup());

        consumer.setNamesrvAddr(consumerConfig.getNameServer());
        consumer.subscribe(consumerConfig.getTopic(), consumerConfig.getSubExpression());

        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            hostName = "";
            log.error(ex.getMessage(), ex);
        }
        consumer.setInstanceName(hostName + RandomDataUtil.randomInt(9999999) + System.currentTimeMillis() + consumerConfig.getTopic());

        consumer.registerMessageListener(messageListenerConcurrently);

        log.info(">>>>> Consumer inited,{}", consumer);

        return consumer;
    }
}
