package com.github.cao.awa.kalmia.event.kalmiagram.handler;

import com.github.cao.awa.kalmia.event.Event;

public interface EventHandler<E extends Event> {
    void handle(E event);
}
