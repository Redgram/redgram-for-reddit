package com.matie.redgram.data.network.api.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by matie on 2016-10-29.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {
    AccessLevel accessLevel() default AccessLevel.ANY;
    Logger logLevel() default Logger.INFO;
}
