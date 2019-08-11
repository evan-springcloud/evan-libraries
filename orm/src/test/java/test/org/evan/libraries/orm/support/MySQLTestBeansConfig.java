package test.org.evan.libraries.orm.support;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created on 2017/7/16.
 *
 * @author evan.shen
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "test.org.evan.libraries.orm.support.config",
        "test.org.evan.libraries.orm.support.jdbc"
})
public class MySQLTestBeansConfig {
}
