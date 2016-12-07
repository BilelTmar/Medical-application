/**
 *
 */
package de.prokimedo;

import java.util.logging.LogManager;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author <a href="mailto:seifert@forwiss.de">Max Seifert</a>
 * @created 2016-10-31
 */
@Configuration
public class ProkimedoConfiguration {

    public ProkimedoConfiguration() {
    }

    @Autowired
    private Environment environment;

    @Value("${prokimedo.server.fqdn}")
    private String serverName = "localhost";

    @Value("${prokimedo.server.port}")
    private String serverPort = "80";

    @Value("${server.context-path}")
    private String serverPrefix = "standards/api";

    public String getServerPrefix() {
        return serverPrefix;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Bean
    public String baseURLHttps() {
        return "https://" + serverName +
                (serverPort.equals("443") ? "" : ":" + serverPort)
                + "/" + serverPrefix;
    }

    @Bean
    public String baseURLHttp() {
        return "http://" + serverName +
                (serverPort.equals("80") ? "" : ":" + serverPort)
                + "/" + serverPrefix;
    }

    public static void configureLogger() {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
