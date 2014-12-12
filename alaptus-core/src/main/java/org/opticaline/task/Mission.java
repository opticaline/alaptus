package org.opticaline.task;

import java.lang.reflect.Method;

/**
 * Created by Nathan on 2014/10/16.
 */
public class Mission {
    private Object object;
    private Method method;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
