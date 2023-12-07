package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.disconnect;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface TryDisconnectEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, String reason);
}
