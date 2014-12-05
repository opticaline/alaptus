package org.opticaline.framework.core.loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by Nathan on 2014/12/4.
 */
public class PluginLoadModule extends AbstractLoadStandards {
    @Override
    public void triggerInterfaces(Class[] classes) {

    }

    @Override
    public void triggerSuperClass(Class cls) {

    }

    @Override
    public void triggerAnnotation(Annotation[] annotations, Class cls) {

    }

    @Override
    public void triggerAnnotation(Annotation[] annotations, Class cls, Method method) {

    }

    @Override
    public void handler(Annotation[] annotations, Class cls, Parameter parameter) {

    }
}