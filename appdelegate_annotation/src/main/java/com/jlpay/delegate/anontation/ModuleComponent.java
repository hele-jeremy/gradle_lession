package com.jlpay.delegate.anontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ModuleComponent {
    //组件初始化的优先级
    int priority() default Integer.MAX_VALUE;
}
