package com.github.cao.awa.kalmia.event.handler;

import com.github.cao.awa.kalmia.event.Event;

public abstract class EventHandler<E extends Event> {
    public abstract void handle(E event);
}
