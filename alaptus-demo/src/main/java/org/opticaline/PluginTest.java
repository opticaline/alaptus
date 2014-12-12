package org.opticaline;

import org.opticaline.alaptus.core.annotation.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Nathan on 2014/12/11.
 */
@Plugin(Plugin.FOR_CLASS)
public class PluginTest {
    private static final Logger logger = LoggerFactory.getLogger(PluginTest.class);

    @Plugin(0)
    public static void test(){
        logger.info("test run !");
    }
}
