package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send.SendMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class SendMessageEventBus extends EventBus<SendMessageEventBusHandler> implements SendMessageEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, SendMessagePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.sessionId(),
                                          packet.keyId(),
                                          packet.signId(),
                                          packet.message(),
                                          packet.sign()
        ));
    }
}
