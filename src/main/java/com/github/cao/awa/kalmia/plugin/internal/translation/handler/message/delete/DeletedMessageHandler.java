package com.github.cao.awa.kalmia.plugin.internal.translation.handler.message.delete;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.delete.DeletedMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeletedMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

@Auto
@PluginRegister(name = "kalmia_translation")
public class DeletedMessageHandler implements DeletedMessageEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, DeletedMessagePacket packet) {
        System.out.println("---Message deleted---");
        System.out.println("UID: " + packet.handler()
                                           .uid());
        System.out.println("SID: " + packet.sessionId());
        System.out.println("SEQ: " + packet.seq());

        router.send(new SelectMessagePacket(123,
                                            0,
                                            114514
        ));
    }
}
