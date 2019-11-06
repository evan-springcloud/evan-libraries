package test.org.evan.libraries.kafka.support;


import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/7/16.
 *
 * @author evan.shen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
        KafkaTestBeansConfig.class,
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class KafkaTestCaseSupport {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected RestTemplate restTemplate = new RestTemplate();

    @Value("${local.server.port}")
    private int port;

    protected String getFullApiUri(String subUrl) {
        return "http://127.0.0.1:" + port + subUrl;
    }
}
