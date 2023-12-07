package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.crypto.ec;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@PluginRegister(name = "kalmia_eventbus")
public class HandshakePreSharedEcEventBus extends EventBus<HandshakePreSharedEcEventBusHandler> implements HandshakePreSharedEcEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, HandshakePreSharedEcPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.cipherField()
        ));

    }
}
