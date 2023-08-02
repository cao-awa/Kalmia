package com.github.cao.awa.kalmia.annotation.actor;

import com.github.cao.awa.apricot.annotation.Stable;
import com.github.cao.awa.apricot.annotation.auto.Auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation mark a method is a setter of a field.
 */
@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Setter {
}
