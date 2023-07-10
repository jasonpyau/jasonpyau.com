package com.jasonpyau.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    public static final long CHEAP_TOKEN = 1;
    public static final long DEFAULT_TOKEN = 2;
    public static final long ADMIN_TOKEN = 5;
    public static final long BIG_TOKEN = 5;
    public static final long LARGE_TOKEN = 10;
    public static final long EXPENSIVE_TOKEN = 25;

    public long value() default DEFAULT_TOKEN;
}
