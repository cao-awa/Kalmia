package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.crypto.aes;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface HandshakeAesCipherEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, byte[] cipher);
}
