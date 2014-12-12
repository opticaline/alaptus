package org.opticaline.alaptus.core.loader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opticaline.alaptus.core.annotation.Plugin;
import org.opticaline.alaptus.utils.ClassUtils;
import org.opticaline.alaptus.utils.ThreadUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        logger.debug(clazz);
        Plugin plugin = (Plugin) clazz.getDeclaredAnnotation(Plugin.class);
        int flag = 0;
        assert plugin != null;
        for (int i : plugin.value()) {
            flag = flag | i;
        }
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if ((flag & Plugin.FOR_CLASS) == Plugin.FOR_CLASS) {
            Method[] methods = ClassUtils.getMethodsByAnnotation(clazz, Plugin.class);
            for (Method method : methods) {
                ThreadUtils.runMethod(object, method);
            }
        }
    }
}
