package test.org.evan.libraries.rocketmq.testcase;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.org.evan.libraries.rocketmq.support.RocketMQTestCaseSupport;
import test.org.evan.libraries.rocketmq.support.model.Demo;
import test.org.evan.libraries.rocketmq.support.model.SexEnum;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Evan.Shen
 * @since 2019-08-19
 */
@Slf4j
public class RocketMQTemplateTest extends RocketMQTestCaseSupport {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void test() throws InterruptedException {
        final AtomicInteger sendCount = new AtomicInteger(0);

        for (int i = 0; i < 20000; i++) {
            Demo demo = new Demo(Long.valueOf(i));

            demo.setFieldText("text" + i);

            if (i % 2 == 0) {
                demo.setFieldRadioEnum(SexEnum.MAN);
            } else {
                demo.setFieldRadioEnum(SexEnum.WOMAN);
            }

            //int topicNo = 2;
            int topicNo = i % 2;

            log.info("Send [no:{} {}]", i, JSON.toJSON(demo));
            //rocketMQTemplate.convertAndSend("TEST_" + topicNo + "_TOPIC", demo);
            rocketMQTemplate.asyncSend("TEST_" + topicNo + "_TOPIC", demo, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    sendCount.getAndAdd(1);
                    //LOGGER.info("传输成功,{}", JSON.toJSONString(sendResult));
                }

                @Override
                public void onException(Throwable e) {
                    LOGGER.error("传输失败", e);
                }
            });

            if (i % 2000 == 0) {
                //Thread.sleep(1000);
                log.info("send {}", i);
            }
        }

        log.info("成功：{}", sendCount.get());

        Thread.sleep(100000l);
    }
}
