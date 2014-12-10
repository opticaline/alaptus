package org.opticaline.alaptus.core.annotation;

import org.opticaline.alaptus.core.config.HttpMethod;

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

    Scope scope() default Scope.SINGLETON;

    enum Scope {
        REQUEST, SESSION, SINGLETON
    }
}
