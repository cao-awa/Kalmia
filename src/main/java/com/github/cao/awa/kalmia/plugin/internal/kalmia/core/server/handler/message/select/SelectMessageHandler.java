package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.SelectMessageEventHandler;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.manage.MessageManager;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class SelectMessageHandler implements SelectMessageEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, SelectMessagePacket packet) {
        long current = packet.from();
        int realSelected;

        MessageManager manager = Kalmia.SERVER.messageManager();

        long curSeq = manager.seq(packet.sessionId());

        if (current > curSeq) {
            return;
        }

        List<Message> messages = ApricotCollectionFactor.arrayList(200);

        long to = Math.min(packet.to(),
                           curSeq
        );

        while (current < to) {
            long selected = Math.min(to - current,
                                     200
            );

            long endSelect = current + selected;

            try {
                manager.operation(packet.sessionId(),
                                  current,
                                  endSelect,
                                  (seq, msg) -> {
                                      messages.add(msg);
                                  }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

            realSelected = messages.size() - 1;

            router.send(new SelectedMessagePacket(packet.sessionId(),
                                                  current,
                                                  current + realSelected,
                                                  messages
            ));

            current += selected + 1;

            messages.clear();
        }
    }
}
