package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface SendMessageEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, long sessionId, long keyId, long signId, byte[] message, byte[] sign);
}
