package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.hello;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.hello.server.ServerHelloEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server.ServerHelloPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

public class ServerHelloEventBus extends EventBus<ServerHelloEventBusHandler> implements ServerHelloEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, ServerHelloPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.testKey(),
                                          packet.testSha(),
                                          packet.iv()
        ));
    }
}
