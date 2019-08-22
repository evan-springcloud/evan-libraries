package test.org.evan.libraries.rocketmq.support.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Evan.Shen
 * @since 2019-08-22
 */
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {
    private String nameServer;

    /**
     *
     */
    public String getNameServer() {
        return nameServer;
    }

    /***/
    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }
}
