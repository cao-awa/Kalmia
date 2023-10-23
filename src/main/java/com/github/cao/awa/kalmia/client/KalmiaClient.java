package com.github.cao.awa.kalmia.client;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

import java.util.function.Consumer;

public class KalmiaClient {
    private Consumer<RequestRouter> activeCallback;
    private RequestRouter router;

    public RequestRouter router() {
        return this.router;
    }

    public void router(RequestRouter router) {
        this.router = router;
    }

    public KalmiaClient activeCallback(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = activeCallback;
        return this;
    }

    public Consumer<RequestRouter> activeCallback() {
        return this.activeCallback;
    }

    public void disconnect() {
        this.router.disconnect();
    }

    public boolean useEpoll() {
        return true;
    }
}
