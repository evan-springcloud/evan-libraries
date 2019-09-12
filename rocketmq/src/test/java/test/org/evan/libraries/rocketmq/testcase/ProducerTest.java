package test.org.evan.libraries.rocketmq.testcase;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import test.org.evan.libraries.rocketmq.support.RocketMQTestCaseSupport;
import test.org.evan.libraries.rocketmq.support.model.Demo;
import test.org.evan.libraries.rocketmq.support.model.SexEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evan.Shen
 * @since 2019-08-22
 */
@Slf4j
public class ProducerTest extends RocketMQTestCaseSupport {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    private ListOperations<String, Map<String, Object>> listOperations;

    @Before
    public void init() {
        listOperations = redisTemplate.opsForList();
    }


    @Test
    public void test() throws InterruptedException {
        int successCount = 0;

        int sendCount = 8000;

        for (int i = 0; i < sendCount; i++) {
            Demo demo = new Demo(Long.valueOf(i));
            demo.setFieldText("text" + i);

            if (i % 2 == 0) {
                demo.setFieldRadioEnum(SexEnum.MAN);
            } else {
                demo.setFieldRadioEnum(SexEnum.WOMAN);
            }

            int topicNo = i % 2;
            //int topicNo = 0;
            //log.info("Send [no:{} {}]", i, JSON.toJSON(demo));
            Message message = new Message("TEST_" + topicNo + "_TOPIC", "", JSON.toJSONBytes(demo));

            try {
                SendResult result = defaultMQProducer.send(message);

                if (SendStatus.SEND_OK.equals(result.getSendStatus())
                        || SendStatus.SLAVE_NOT_AVAILABLE.equals(result.getSendStatus())
                        || SendStatus.FLUSH_SLAVE_TIMEOUT.equals(result.getSendStatus())
                        || SendStatus.FLUSH_DISK_TIMEOUT.equals(result.getSendStatus())
                ) {
                    Map<String, Object> map = new HashMap();

                    MessageQueue messageQueue = result.getMessageQueue();

                    map.put("broker", messageQueue.getBrokerName());
                    map.put("topic", messageQueue.getTopic());
                    map.put("region", result.getRegionId());

                    listOperations.rightPush("total_send", map);
                    listOperations.rightPush(messageQueue.getBrokerName() + "_" + messageQueue.getTopic() + "_send", map);

                    successCount++;

                    if (!SendStatus.SEND_OK.equals(result.getSendStatus())){
                        log.warn(result.getSendStatus().name());
                    }
                } else {
                    log.error("传输失败," + result.getSendStatus());
                }
            } catch (MQClientException | RemotingException | MQBrokerException ex) {
                log.error("传输失败，id = " + demo.getId(), ex);
            }
            if (i % 500 == 0) {
                //Thread.sleep(1500);
                log.info("send {}", i);
            }
        }

        log.info("成功：{}", successCount);

        Thread.sleep(5000l);
    }
}
