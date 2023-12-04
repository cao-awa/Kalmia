package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.SelectedMessageEventHandler;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.plains.PlainsMessage;
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

import java.util.Arrays;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class SelectedMessageHandler implements SelectedMessageEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SelectedMessagePacket packet) {
        System.out.println("Received msg of session " + packet.sessionId());
        System.out.println("Range is " + packet.from() + " to " + packet.to());

        for (Message message : packet.messages()) {
            if (message instanceof PlainsMessage plain) {
                System.out.println("PLAINS: " + plain.msg());
            } else if (message instanceof DeletedMessage deleted) {
                System.out.println("DELETED: " + deleted.digest()
                                                        .value36());
            } else if (message instanceof UnknownMessage unknown) {
                System.out.println("UNKNOWN: " + Arrays.toString(unknown.details()));
            }
        }
    }
}
