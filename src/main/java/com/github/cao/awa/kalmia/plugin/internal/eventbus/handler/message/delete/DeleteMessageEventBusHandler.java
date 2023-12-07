package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.delete;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface DeleteMessageEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, long sessionId, long seq);
}
