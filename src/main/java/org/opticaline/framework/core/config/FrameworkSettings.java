package org.opticaline.framework.core.config;

import org.dom4j.DocumentException;

/**
 * Created by Nathan on 14-8-25.
 */
public class FrameworkSettings extends XMLSettings {
    public FrameworkSettings(String settings) throws DocumentException {
        this.setXMLPath(settings);
        this.load();
    }

    public String getTemplatePath() {
        return this.getProperty(SettingString.TEMPLATE_PATH);
    }

}
