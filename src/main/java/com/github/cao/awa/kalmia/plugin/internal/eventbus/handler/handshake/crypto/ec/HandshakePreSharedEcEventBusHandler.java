package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.crypto.ec;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface HandshakePreSharedEcEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, String cipherField);
}
