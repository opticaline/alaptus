package org.opticaline.alaptus.core.loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by Nathan on 2014/12/4.
 */
public interface LoadStandard {

    public void init();

    public void handler(Class[] classes);

    public void triggerInterfaces(Class[] classes);

    public void handler(Class cls);

    public void triggerSuperClass(Class cls);

    public void handler(Annotation[] annotations, Class cls);

    public void triggerAnnotation(Annotation[] annotations, Class cls);

    public void handler(Annotation[] annotations, Class cls, Method method);

    public void triggerAnnotation(Annotation[] annotations, Class cls, Method method);

    public void handler(Annotation[] annotations, Class cls, Parameter parameter);

    public void triggerAnnotation(Annotation[] annotations, Class cls, Parameter parameter);

}
