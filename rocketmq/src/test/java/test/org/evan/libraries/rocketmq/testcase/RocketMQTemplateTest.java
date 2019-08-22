package test.org.evan.libraries.rocketmq.testcase;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.org.evan.libraries.rocketmq.support.RocketMQTestCaseSupport;
import test.org.evan.libraries.rocketmq.support.model.Demo;
import test.org.evan.libraries.rocketmq.support.model.SexEnum;

/**
 * @author Evan.Shen
 * @since 2019-08-19
 */
public class ProducerTest extends RocketMQTestCaseSupport {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void test() {
        for (int i = 0; i < 8; i++) {
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

            rocketMQTemplate.asyncSend("TEST_" + topicNo + "_TOPIC", demo,new SendCallback(){
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


//        DefaultMQProducer producer = rocketMQTemplate.getProducer();
//        LOGGER.info("producer: {}",producer);
    }
}
