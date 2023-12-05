package com.github.cao.awa.kalmia.event;

public abstract class Event {
    public String name() {
        return getClass().getSimpleName();
    }
}
