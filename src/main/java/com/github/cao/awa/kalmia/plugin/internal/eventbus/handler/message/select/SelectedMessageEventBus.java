package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.SelectedMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

public class SelectedMessageEventBus extends EventBus<SelectedMessageEventBusHandler> implements SelectedMessageEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SelectedMessagePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.sessionId(),
                                          packet.from(),
                                          packet.to(),
                                          packet.sessionCurSeq(),
                                          packet.messages()
        ));
    }
}
