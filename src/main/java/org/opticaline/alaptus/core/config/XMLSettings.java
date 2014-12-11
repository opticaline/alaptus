package org.opticaline.alaptus.core.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.XMLBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Created by Nathan on 14-8-25.
 */
public class XMLSettings {
    private String[] paths;
    private Configuration configuration;

    public void setXMLPath(String... paths) {
        this.paths = paths;
    }

    public String[] getXMLPath() {
        return this.paths;
    }

    public void load() throws ConfigurationException {
        XMLBuilderParameters params = new Parameters().xml();
        for (String string : paths) {
            params.setFileName(string);
        }
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
                        .configure(params.setValidating(false));
        configuration = builder.getConfiguration();
    }

    public String get(String path) {
        return configuration.getString(path);
    }

}
