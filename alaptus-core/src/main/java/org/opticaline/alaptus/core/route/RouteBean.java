package org.opticaline.alaptus.core.route;

import java.lang.reflect.Method;

/**
 * Created by Nathan on 14-8-27.
 */
public class RouteBean {
    public RouteBean(Object clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    private Method method;
    private Object clazz;
    private String[] paramNames;
    private Class[] paramTypes;
    private boolean[] paramNotNull;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getClazz() {
        return clazz;
    }

    public void setClazz(Object clazz) {
        this.clazz = clazz;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public void setParamNotNull(boolean[] paramNotNull) {
        this.paramNotNull = paramNotNull;
    }

    public Class getParamType(int i) {
        return this.paramTypes[i];
    }

    public String getParamNames(int i) {
        return this.paramNames[i];
    }

    public boolean getParamNotNull(int i) {
        return this.paramNotNull[i];
    }
}
