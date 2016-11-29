package de.prokimedo.test;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.prokimedo.Application;
import de.prokimedo.ProkimedoConfiguration;

/**
 * @author <a href="mailto:seifert@forwiss.de">Max Seifert</a>
 * @created 2016-11-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class ConfigurationTest {

    @Autowired
    ProkimedoConfiguration configuration;


    @Before
    public void setUp() throws Exception {
        assertNotNull(configuration);
        assertNotNull(configuration.getEnvironment());

    }

    @Test
    public void log4jTest() {

        Logger logger = Logger.getRootLogger();
        logger.fatal("This is a fatal log test");

    }

}
