package test.org.evan.libraries.kafka.support.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.org.evan.libraries.kafka.support.consumer.ConsumerDataStater;
import test.org.evan.libraries.kafka.support.model.Demo;
import test.org.evan.libraries.kafka.support.model.SexEnum;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evan.Shen
 * @since 2019-11-03
 */
@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProducerDataStater producerDataStater;

    private ListOperations<String, Map<String, Object>> listOperations;

    @PostConstruct
    public void init() {
        listOperations = redisTemplate.opsForList();

        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
            @Override
            public void onSuccess(String topic, Integer partition, String key, String value,
                                  RecordMetadata recordMetadata) {
                Map<String, Object> map = new HashMap();
                map.put("topic", topic);
                map.put("partition", recordMetadata.partition());
                map.put("key", key);
                map.put("value", value);

                listOperations.rightPush("send_total", map);
                listOperations.rightPush("send_" + topic, map);
                //listOperations.rightPush(topic + "_" + recordMetadata.partition() + "_send", map);

                producerDataStater.statSuccess(recordMetadata);
            }

            @Override
            public void onError(String topic, Integer partition, String key, String value, Exception exception) {
                Map<String, Object> map = new HashMap();
                map.put("topic", topic);
                map.put("key", key);
                map.put("value", value);
                map.put("exception", exception);

                listOperations.rightPush("send_failure_total", map);
                listOperations.rightPush("send_failure_" + topic, map);

                log.warn("send error, topic [" + topic + "], partition [" + partition + "], key [" + key + "]" + exception.getMessage());

                producerDataStater.statFail(topic);
            }
        });
    }

    @PostMapping("send")
    public String send(@RequestParam(name = "count", required = false, defaultValue = "10000") int sendCount) {
        for (int i = 0; i < sendCount; i++) {
            Demo demo = new Demo(Long.valueOf(i));
            demo.setFieldText("text" + i);
            if (i % 2 == 0) {
                demo.setFieldRadioEnum(SexEnum.MAN);
            } else {
                demo.setFieldRadioEnum(SexEnum.WOMAN);
            }

            int topicNo = i % 2;

            String topic = "test_topic_" + topicNo;

            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, demo.getId() + "", JSON.toJSONString(demo));

            future.addCallback(new ListenableFutureCallback() {
                @Override
                public void onSuccess(Object o) {
                    // log.info(o + "");
                }
                @Override
                public void onFailure(Throwable throwable) {
                   log.info(throwable.getMessage());
                }
            });
        }

        return "success";
    }
}
