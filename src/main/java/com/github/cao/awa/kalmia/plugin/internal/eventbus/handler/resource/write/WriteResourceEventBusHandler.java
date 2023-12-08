package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.resource.write;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface WriteResourceEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, byte[] data, boolean isFinal);
}
