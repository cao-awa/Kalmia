package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.FetchMessageEventHandler;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.FetchMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.FetchedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class FetchMessageHandler implements FetchMessageEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, FetchMessagePacket packet) {
        List<Message> result = ApricotCollectionFactor.arrayList();

        packet.identities().forEach(identity -> {
            Message message = Kalmia.SERVER.getMessageManager().get(identity);
            if (message == null) {
                return;
            }
            result.add(message);
        });

        router.send(new FetchedMessagePacket(result).receipt(packet.receipt()));
    }
}
