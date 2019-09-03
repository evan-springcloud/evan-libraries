package test.org.evan.libraries.rocketmq.support;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created on 2017/7/16.
 *
 * @author evan.shen
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "test.org.evan.libraries.rocketmq.support.config",
        "org.apache.rocketmq.spring.autoconfigure"
})
public class RocketMQTestBeansConfig {
}
