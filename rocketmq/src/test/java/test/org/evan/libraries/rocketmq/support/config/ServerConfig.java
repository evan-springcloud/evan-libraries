package test.org.evan.libraries.rocketmq.support.config;

import lombok.extern.slf4j.Slf4j;
import org.evan.libraries.utils.RandomDataUtil;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Evan.Shen
 * @since 2019-08-24
 */
@Slf4j
@Component
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent applicationEvent) {
        this.serverPort = applicationEvent.getWebServer().getPort();
        log.info("Web Server port is {}", serverPort);
    }

    public int getServerPort() {
        if (serverPort == 0) {
            serverPort = RandomDataUtil.randomInt(99999);
        }

        return serverPort;
    }
}
