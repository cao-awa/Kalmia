package com.github.cao.awa.kalmia.client;

import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

import java.util.function.Consumer;

public class KalmiaClient {
    private Consumer<UnsolvedRequestRouter> activeCallback;
    private boolean activated;

    public KalmiaClient activeCallback(Consumer<UnsolvedRequestRouter> activeCallback) {
        this.activeCallback = router -> {
            activeCallback.accept(router);
            this.activated = true;
        };
        return this;
    }

    public Consumer<UnsolvedRequestRouter> activeCallback() {
        return this.activeCallback;
    }

    public final void block() {
        while (! this.activated) {
            TimeUtil.coma(10);
        }
    }
}
