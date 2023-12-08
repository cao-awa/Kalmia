package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.chat.session.in;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.in.ChatInSessionEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in.ChatInSessionPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

public class ChatInSessionEventBus extends EventBus<ChatInSessionEventBusHandler> implements ChatInSessionEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, ChatInSessionPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.targetUid(),
                                          packet.sessionId()
        ));
    }
}
