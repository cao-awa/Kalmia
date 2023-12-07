package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface SendMessageRefusedEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, String reason);
}
