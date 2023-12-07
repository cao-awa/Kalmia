package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.feedback;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface LoginFailureEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, long uid, String reason);
}
