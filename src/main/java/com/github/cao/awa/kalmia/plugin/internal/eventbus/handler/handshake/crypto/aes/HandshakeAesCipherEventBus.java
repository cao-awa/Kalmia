package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.crypto.aes;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.crypto.aes.HandshakeAesCipherEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class HandshakeAesCipherEventBus extends EventBus<HandshakeAesCipherEventBusHandler> implements HandshakeAesCipherEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, HandshakeAesCipherPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.cipher()
        ));
    }
}
