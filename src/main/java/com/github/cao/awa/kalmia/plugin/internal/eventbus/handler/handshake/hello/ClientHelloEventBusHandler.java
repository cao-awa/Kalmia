package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.hello;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;

public interface ClientHelloEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, RequestProtocol majorProtocol, String clientVersion, String expectCipherField);
}
