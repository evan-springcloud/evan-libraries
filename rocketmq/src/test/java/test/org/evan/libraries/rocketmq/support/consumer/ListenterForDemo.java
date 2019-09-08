package test.org.evan.libraries.rocketmq.support.consumer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import test.org.evan.libraries.rocketmq.support.model.Demo;

import java.util.List;

/**
 * @author Evan.Shen
 * @since 2019-08-30
 */
@Slf4j
public class ListenterForDemo implements MessageListenerConcurrently {

    private ConsumerDataStater consumerDataStater;

    public ListenterForDemo(ConsumerDataStater consumerDataStater) {
        this.consumerDataStater = consumerDataStater;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        //log.info(context.getMessageQueue() + "");
        for (MessageExt e : msgs) {
            Demo demo = JSON.parseObject(e.getBody(), Demo.class);

            if(log.isTraceEnabled()){
                log.trace(JSON.toJSONString(demo));
            }

            if (demo != null && demo.getId() != null) {
                if (demo.getId() % 15 == 0) {
                    try {
                        Thread.sleep(1500); //模拟消费慢
                        //log.info(">>>>>>>>>>>>>> stoping");
                    } catch (InterruptedException ex) {
                        //log.warn(ex.getMessage(),ex);
                    }
                }
            }
        }

        consumerDataStater.stat(msgs,context);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
