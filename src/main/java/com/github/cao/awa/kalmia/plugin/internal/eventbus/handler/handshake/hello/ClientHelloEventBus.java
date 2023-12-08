package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.hello.client.ClientHelloEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class ClientHelloEventBus extends EventBus<ClientHelloEventBusHandler> implements ClientHelloEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, ClientHelloPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.majorProtocol(),
                                          packet.clientVersion(),
                                          packet.expectCipherField()
        ));
    }
}
