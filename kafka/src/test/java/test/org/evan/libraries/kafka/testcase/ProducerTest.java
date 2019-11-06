package test.org.evan.libraries.kafka.testcase;

        import lombok.extern.slf4j.Slf4j;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.context.annotation.ComponentScan;

/**
 * @author Evan.Shen
 * @since 2019-08-22
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "test.org.evan.libraries.kafka.support.config",
        "test.org.evan.libraries.kafka.support.producer",
})
public class ProducerTest {
    public static void main(String[] args) {
        SpringApplication.run(ProducerTest.class, args);
    }
}
