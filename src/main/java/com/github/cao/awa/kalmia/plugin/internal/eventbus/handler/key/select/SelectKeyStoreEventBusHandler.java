package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.key.select;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

import java.util.List;

public interface SelectKeyStoreEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, List<Long> ids);
}
