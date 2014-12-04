package org.opticaline.framework.core.loader;

/**
 * Created by Nathan on 2014/12/4.
 */
public class PluginLoadModule implements LoadStandard {
    @Override
    public void byInterface(Class cls) {
        if (cls.isAssignableFrom(PluginStandard.class)) {
            // TODO
            System.out.println();
        }
    }
}