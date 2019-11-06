package test.org.evan.libraries.kafka.testcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Evan.Shen
 * @since 2019-08-19
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "test.org.evan.libraries.kafka.support.config",
        "test.org.evan.libraries.kafka.support.consumer",
})
public class ConsumerTest {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerTest.class, args);
    }
}
