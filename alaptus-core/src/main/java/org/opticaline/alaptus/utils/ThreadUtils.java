package org.opticaline.alaptus.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Nathan on 2014/12/12.
 */
public class ThreadUtils {
    public static void runMethod(Object object, Method method, Object... args) {
        new Thread(() -> {
            try {
                method.invoke(object, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }).run();
    }
}
