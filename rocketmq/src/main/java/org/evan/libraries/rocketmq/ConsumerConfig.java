package org.evan.libraries.rocketmq;

import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author Evan.Shen
 * @since 2019-08-26
 */
@Setter
@Getter
public class ConsumerConfig {
    private String nameServer;
    private String group;
    private String topic;
    private String subExpression;
    private MessageModel messageModel = MessageModel.CLUSTERING;

}
