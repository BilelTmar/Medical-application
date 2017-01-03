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

    @Autowired
    private Environment environment;

    @Value("${prokimedo.server.fqdn:localhost}")
    private String serverName;

    @Value("${local.server.port:0}")
    private int localServerPort;

    @Value("${prokimedo.server.port:80}")
    private String serverPort;

    @Value("${server.context-path:standards/api}")
    private String serverPrefix;

    @Value("${spring.profiles.active:development}")
    private String springProfilesActive;

    @Value("${logging.level.production:WARN}")
    private String loggingLevelProduction;

    @Value("${logging.level.development:INFO}")
    private String loggingLevelDevelopment;

    public String getServerPrefix() {
        return serverPrefix;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Bean
    public String baseURLHttps() {
        if (localServerPort > 0) {
            return "https://" + serverName + ":" + localServerPort
                    + "/" + serverPrefix;
        }
        return "https://" + serverName +
                (serverPort.equals("443") ? "" : ":" + serverPort)
                + "/" + serverPrefix;
    }

    @Bean
    public String baseURLHttp() {
        if (localServerPort > 0) {
            return "http://" + serverName + ":" + localServerPort
                    + "/" + serverPrefix;
        }
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
