package org.opticaline.framework.core.loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Nathan on 2014/12/5.
 */
public abstract class AbstractLoadStandards implements LoadStandard {
    protected Class[] intFaces;
    protected Class superClass;
    protected Annotation[] atClass;
    protected Annotation[] atMethod;
    protected Annotation[] atParameter;

    AbstractLoadStandards() {

    }

    private boolean contain(Class cls, Class... classes) {
        for (Class c : classes) {
            if (cls.isAssignableFrom(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void handler(Class[] classes) {
    }

    @Override
    public void handler(Class cls) {

    }

    @Override
    public void handler(Annotation[] annotations, Class cls) {

    }

    @Override
    public void handler(Annotation[] annotations, Class cls, Method method) {

    }
}
