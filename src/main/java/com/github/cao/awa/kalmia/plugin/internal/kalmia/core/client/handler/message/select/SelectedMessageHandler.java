package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.select;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.select.SelectedMessageEventHandler;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.PlainMessage;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_core")
public class SelectedMessageHandler implements SelectedMessageEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SelectedMessagePacket packet) {
        System.out.println("Received msg of session " + packet.sessionId());
        System.out.println("Range is " + packet.from() + " to " + packet.to());

        for (Message message : packet.messages()) {
            if (message instanceof PlainMessage plain) {
                System.out.println("PLAINS: " + plain.getMsg());
            } else if (message instanceof DeletedMessage deleted) {
                System.out.println("DELETED: " + deleted.digest()
                                                        .value36());
            }
        }
    }
}
