package org.opticaline.alaptus.core.loader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Nathan on 2014/12/11.
 */
public class PluginHandler {
    private static final Log logger = LogFactory.getLog(PluginHandler.class);
    private Class clazz;

    public PluginHandler(Class clazz) {
        this.clazz = clazz;
    }

    public void handler() {
        logger.info(clazz);
    }
}
