package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.hello;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface ServerHelloEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, byte[] testKey, byte[] testSha, byte[] iv);
}
