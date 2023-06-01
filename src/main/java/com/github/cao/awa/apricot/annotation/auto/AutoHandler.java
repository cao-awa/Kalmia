package com.github.cao.awa.apricot.annotation.auto;

import com.github.cao.awa.apricot.annotation.Stable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoHandler {
    Class<?> value();
}
