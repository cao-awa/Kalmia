package com.github.cao.awa.kalmia.function.provider;

import java.util.function.Consumer;

public class Consumers {
    public static final Consumer<?> DO_NOTHING = e -> {
    };

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> doNothing() {
        return (Consumer<T>) DO_NOTHING;
    }
}
