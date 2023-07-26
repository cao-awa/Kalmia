package com.github.cao.awa.kalmia.annotation.auto.event;

import com.github.cao.awa.apricot.annotation.Stable;
import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.event.network.NetworkEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NetworkEventTarget {
    Class<? extends NetworkEvent> value();
}
