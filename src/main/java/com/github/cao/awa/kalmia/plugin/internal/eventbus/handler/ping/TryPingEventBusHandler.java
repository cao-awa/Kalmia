package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.ping;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface TryPingEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, long startTime);
}
