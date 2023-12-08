package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send.SentMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

public class SentMessageEventBus extends EventBus<SentMessageEventBusHandler> implements SentMessageEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SentMessagePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.seq()
        ));
    }
}
