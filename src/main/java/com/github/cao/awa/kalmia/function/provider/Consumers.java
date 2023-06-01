package com.github.cao.awa.kalmia.function.provider;

import java.util.function.Consumer;

public class Consumers {
    public static final Consumer<?> DO_NOTHING = e -> {
    };
    public static final Consumer<?> DO_PRINT = System.out :: println;

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> doNothing() {
        return (Consumer<T>) DO_NOTHING;
    }

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> doPrint() {
        return (Consumer<T>) DO_PRINT;
    }
}
