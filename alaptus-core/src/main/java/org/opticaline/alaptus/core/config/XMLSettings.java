package org.opticaline.alaptus.core.config;

import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Created by Nathan on 14-8-25.
 */
public class XMLSettings {
    private String[] paths;

    public void setXMLPath(String... paths) {
        this.paths = paths;
    }

    public String[] getXMLPath() {
        return this.paths;
    }

    public void load() throws ConfigurationException {
        ///XMLBuilderParameters params = new Parameters().xml();
        for (String string : paths) {
            //System.out.println(string);
            ///params.setFileName(string);
        }

        //FileBasedConfigurationBuilder<XMLConfiguration> builder =
        //        new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
        //                .configure(params.setValidating(false));
        //configuration = builder.getConfiguration();
    }

    public String get(String path) {
        //添加json配置文件的实现之后改为从json文件读取配置
        return "org.opticaline";
    }

}
