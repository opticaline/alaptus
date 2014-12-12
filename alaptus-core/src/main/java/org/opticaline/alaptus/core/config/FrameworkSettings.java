package org.opticaline.alaptus.core.config;

import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Created by Nathan on 14-8-25.
 */
public class FrameworkSettings extends XMLSettings {
    public FrameworkSettings(String settings) throws ConfigurationException {
        this.setXMLPath(settings);
        this.load();
    }
}
