package com.github.cao.awa.kalmia.annotation.inaction;

import com.github.cao.awa.apricot.annotation.Stable;
import com.github.cao.awa.apricot.annotation.auto.Auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation mark a method should not be set value of this field or variable.
 */
@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface DoNotSet {
}
