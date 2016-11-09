/**
 *
 */
package de.prokimedo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author <a href="mailto:seifert@forwiss.de">Max Seifert</a>
 * @created 2016-10-31
 */
@Configuration
@ComponentScan(basePackageClasses = {ProkimedoConfiguration.class, Application.class})
//@Singleton
public class ProkimedoConfiguration {

//    @Value("${prokimedo.tdb.url")
//    private String prokimedoTdbUrl = "TDB";


    public ProkimedoConfiguration() {
    }

    @Autowired
    private Environment environment;

    @Value("${prokimedo.server.fqdn}")
    private String serverName = "localhost";

    @Value("${prokimedo.server.prefix}")
    private String prefix = "prokimedo/standards";

    public String namespaceHttps() {
        return "https://" + serverName + "/" + prefix;
    }

    @Bean
    public String namespaceHttp() {
        return "http://" + serverName + "/" + prefix;
    }
}