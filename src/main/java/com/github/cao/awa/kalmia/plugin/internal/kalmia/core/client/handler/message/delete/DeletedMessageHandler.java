package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.delete;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.delete.DeletedMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeletedMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_core")
public class DeletedMessageHandler implements DeletedMessageEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, DeletedMessagePacket packet) {
        System.out.println("---Message deleted---");
        System.out.println("UID: " + packet.handler()
                                           .getUid());
        System.out.println("SID: " + packet.sessionId());
        System.out.println("SEQ: " + packet.seq());

        router.send(new SelectMessagePacket(123,
                                            0,
                                            114514
        ));
    }
}
