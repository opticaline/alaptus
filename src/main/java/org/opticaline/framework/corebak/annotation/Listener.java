package org.opticaline.framework.corebak.annotation;

import org.opticaline.framework.corebak.config.ListenerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Nathan on 14-8-28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Listener {
    ListenerType value();

    String forRequestUri() default "";

    String forRouteMethod() default "";
}