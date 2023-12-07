package com.github.cao.awa.kalmia.plugin.internal.eventbus;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

import java.util.List;
import java.util.function.Consumer;

public abstract class EventBus<T extends EventBusHandler> {
    private final List<T> handlers = ApricotCollectionFactor.arrayList();

    public void trigger(T handler) {
        this.handlers.add(handler);
    }

    public void trigger(Consumer<T> consumer) {
        this.handlers.forEach(consumer);
    }
}
