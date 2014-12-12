package org.opticaline;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.XMLBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Created by Nathan on 2014/12/11.
 */
public class LogTest {
    private static Log log = LogFactory.getLog(LogTest.class);

    public static void main(String[] args) throws ConfigurationException {
        SLF4JBridgeHandler.install();
        log.error("ERROR");
        log.debug("DEBUG");
        log.warn("WARN");
        log.info("INFO");
        log.trace("TRACE");
        XMLBuilderParameters params = new Parameters().xml();
        params.setFileName("D:\\projects\\alaptus_test\\src\\main\\resources\\application.xml");
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
                        .configure(params.setValidating(false));
        Configuration configuration = builder.getConfiguration();

    }
}
