package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.send.SentMessageEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_core")
public class SentMessageHandler implements SentMessageEventHandler {
    @Client
    @Override
    public void handle(RequestRouter router, SentMessagePacket packet) {
        System.out.println("UID: " + packet.handler()
                                           .uid());
        System.out.println("IDT: " + Mathematics.radix(packet.receipt(),
                                                       36
        ));
        System.out.println("SEQ: " + packet.seq());
    }
}
