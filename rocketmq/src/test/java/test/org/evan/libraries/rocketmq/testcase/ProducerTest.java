package test.org.evan.libraries.rocketmq.testcase;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.org.evan.libraries.rocketmq.support.RocketMQTestCaseSupport;
import test.org.evan.libraries.rocketmq.support.model.Demo;
import test.org.evan.libraries.rocketmq.support.model.SexEnum;

/**
 * @author Evan.Shen
 * @since 2019-08-22
 */
public class ProducerTest extends RocketMQTestCaseSupport {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Test
    public void test() throws MQClientException, RemotingException, InterruptedException {
        for (int i = 0; i < 20; i++) {
            Demo demo = new Demo(Long.valueOf(i));

            demo.setFieldText("text" + i);

            if (i % 2 == 0) {
                demo.setFieldRadioEnum(SexEnum.MAN);
            } else {
                demo.setFieldRadioEnum(SexEnum.WOMAN);
            }

            //int topicNo = i % 3;

            LOGGER.info("Send: {}", JSON.toJSON(demo));

            int topicNo = 0;
            //rocketMQTemplate.convertAndSend("TEST_" + topicNo + "_TOPIC", demo);

            Message message = new Message("TEST_" + topicNo + "_TOPIC", "", (JSON.toJSON(demo) + "").getBytes());

            defaultMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    LOGGER.info("传输成功");
                    LOGGER.info(JSON.toJSONString(sendResult));
                }

                @Override
                public void onException(Throwable e) {
                    LOGGER.error("传输失败", e);
                }
            });
        }

        Thread.sleep(10000l);
    }
}
