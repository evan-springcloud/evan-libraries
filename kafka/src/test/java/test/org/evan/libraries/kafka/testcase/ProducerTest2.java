package test.org.evan.libraries.kafka.testcase;

import org.junit.Test;
import test.org.evan.libraries.kafka.support.KafkaTestCaseSupport;

/**
 * @author Evan.Shen
 * @since 2019-11-04
 */
public class ProducerTest2 extends KafkaTestCaseSupport {

    @Test
    public void testSend() {
        String url = getFullApiUri("/send?count=10000");

        for (int i = 0; i < 45; i++) {
            String result = restTemplate.postForObject(url, null, String.class);
            LOGGER.info("========>>：" + result);
        }
    }
}
