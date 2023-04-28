package com.github.cao.awa.kalmia.client;

import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

import java.util.function.Consumer;

public class KalmiaClient {
    private Consumer<RequestRouter> activeCallback;
    private boolean activated;

    public KalmiaClient activeCallback(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = router -> {
            activeCallback.accept(router);
            this.activated = true;
        };
        return this;
    }

    public Consumer<RequestRouter> activeCallback() {
        return this.activeCallback;
    }

    public final void block() {
        while (! this.activated) {
            TimeUtil.coma(10);
        }
    }

    public boolean useEpoll() {
        return true;
    }
}
