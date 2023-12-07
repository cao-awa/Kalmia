package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.sign;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface LoginWithSignEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, long uid, byte[] challengeData);
}
