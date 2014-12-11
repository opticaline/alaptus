package org.opticaline.alaptus.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Nathan on 2014/12/11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {
    int[] value();

    int FOR_CLASS = 1;
    int FOR_FIELD = 2;
    int FOR_METHOD = 4;
    int FOR_METHOD_PARAM = 8;
}
