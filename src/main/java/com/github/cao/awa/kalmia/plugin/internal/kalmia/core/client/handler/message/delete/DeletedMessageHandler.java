package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.delete;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.delete.DeletedMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeletedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class DeletedMessageHandler implements DeletedMessageEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, DeletedMessagePacket packet) {
        System.out.println("---Message deleted---");
        System.out.println("UID: " + packet.handler()
                                           .accessIdentity());
        System.out.println("SID: " + packet.sessionIdentity());
        System.out.println("SEQ: " + packet.seq());
    }
}
