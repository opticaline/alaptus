package org.opticaline.framework.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Nathan on 14-8-19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    String value();

    boolean notNull() default false;

    //原定根据变量类型定义的转换方法制定参数，没有现在的自动寻找方便而废弃
    //Class transaction() default NoneType.class;
}
