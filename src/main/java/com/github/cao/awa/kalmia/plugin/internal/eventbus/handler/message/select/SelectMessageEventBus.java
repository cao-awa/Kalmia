package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.SelectMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class SelectMessageEventBus extends EventBus<SelectMessageEventBusHandler> implements SelectMessageEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, SelectMessagePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.sessionIdentity(),
                                          packet.from(),
                                          packet.to()
        ));
    }
}
