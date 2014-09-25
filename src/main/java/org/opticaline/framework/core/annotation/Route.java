package org.opticaline.framework.core.annotation;

import org.opticaline.framework.core.config.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Nathan on 14-8-19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
    String value();

    HttpMethod method() default HttpMethod.GET;
}
