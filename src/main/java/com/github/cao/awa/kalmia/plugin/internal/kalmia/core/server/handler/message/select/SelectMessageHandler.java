package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.SelectMessageEventHandler;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.manager.MessageManager;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.lilium.catheter.Catheter;
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
        long start = packet.from();
        int realSelected;

        MessageManager manager = Kalmia.SERVER.messageManager();

        Session session = Kalmia.SERVER.sessionManager()
                                       .session(packet.sessionIdentity());

        if (session == null || ! session.accessible(router.accessIdentity())) {
            return;
        }

        long currentSeqEnd = manager.seq(packet.sessionIdentity());

        List<Message> messages = ApricotCollectionFactor.arrayList();

        System.out.println(start + ":" + packet.to() + "(" + currentSeqEnd + ")");

        if (start > packet.to() || start > currentSeqEnd || currentSeqEnd == - 1) {
            router.send(new SelectedMessagePacket(packet.sessionIdentity(),
                                                  - 1,
                                                  - 1,
                                                  currentSeqEnd,
                                                  messages
            ).receipt(packet.receipt()));

            return;
        }

        if (start == packet.to() || start == currentSeqEnd) {
            messages.add(manager.get(packet.sessionIdentity(),
                                     packet.from()
            ));

            router.send(new SelectedMessagePacket(packet.sessionIdentity(),
                                                  packet.from(),
                                                  packet.from(),
                                                  currentSeqEnd,
                                                  messages
            ).receipt(packet.receipt()));

            return;
        }

        long end = Math.min(packet.to(),
                            currentSeqEnd
        );

        while (start < end) {
            long selected = Math.min(end - start,
                                     200
            );

            long endSelect = start + selected;

            try {
                manager.operation(packet.sessionIdentity(),
                                  start,
                                  endSelect,
                                  (seq, msg) -> {
                                      System.out.println(msg);
                                      messages.add(msg);
                                  }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

            realSelected = messages.size() - 1;

            router.send(new SelectedMessagePacket(packet.sessionIdentity(),
                                                  start,
                                                  start + realSelected,
                                                  currentSeqEnd,
                                                  // Copy the list, because it will be cleared immediately.
                                                  Catheter.of(messages)
                                                          .list()
            ).receipt(packet.receipt()));

            start += selected + 1;

            messages.clear();
        }
    }
}
