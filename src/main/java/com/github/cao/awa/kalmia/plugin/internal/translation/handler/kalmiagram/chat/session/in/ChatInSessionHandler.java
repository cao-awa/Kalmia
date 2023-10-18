package com.github.cao.awa.kalmia.plugin.internal.translation.handler.kalmiagram.chat.session.in;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.in.ChatInSessionEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in.ChatInSessionPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

@Auto
@PluginRegister(name = "kalmia_translation")
public class ChatInSessionHandler implements ChatInSessionEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, ChatInSessionPacket packet) {
        System.out.println("Requested session for " + packet.targetUid() + ": " + packet.sessionId());
    }
}
