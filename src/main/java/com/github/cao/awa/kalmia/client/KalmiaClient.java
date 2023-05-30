package com.github.cao.awa.kalmia.client;

import com.github.cao.awa.kalmia.network.router.RequestRouter;

import java.util.function.Consumer;

public class KalmiaClient {
    private Consumer<RequestRouter> activeCallback;

    public KalmiaClient activeCallback(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = activeCallback;
        return this;
    }

    public Consumer<RequestRouter> activeCallback() {
        return this.activeCallback;
    }

    public boolean useEpoll() {
        return true;
    }
}
