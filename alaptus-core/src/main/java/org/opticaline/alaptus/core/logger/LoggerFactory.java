package org.opticaline.alaptus.core.logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Nathan on 2014/12/6.
 */
@Deprecated
public class LoggerFactory {
    private static String[] factoryPaths = new String[]{
            "org.slf4j.LoggerFactory",
            "org.apache.log4j.Logger",
            "java.util.logging.Logger"
    };
    private static Class loggerFactory;

    private static ClassLoader loader = ClassLoader.getSystemClassLoader();


    static {
        init();
    }

    private static void init() {
        for (String factoryPath : factoryPaths) {
            try {
                loggerFactory = loader.loadClass(factoryPath);
                break;
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    private static Object makeLogger(String name) {
        try {
            Method getLogger = loggerFactory.getMethod("getLogger", String.class);
            return getLogger.invoke(null, name);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Logger getLogger(String name) {
        Class[] classes = new Class[]{Logger.class};
        final Object logger = makeLogger(name);
        return (Logger) Proxy.newProxyInstance(loader, classes, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Class[] paramTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypes[i] = args[i].getClass();
                }
                Method m = logger.getClass().getMethod(method.getName(), paramTypes);
                m.invoke(logger, args[0]);
                return null;
            }
        });
    }

    public static Logger getLogger(Class<org.opticaline.alaptus.utils.ClassUtils> clazz) {
        return getLogger(clazz.toString());
    }
}
