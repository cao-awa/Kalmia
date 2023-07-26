package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.manage.MessageManager;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@AutoSolvedPacket(12)
public class SelectMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long sessionId;
    @AutoData
    private long from;
    @AutoData
    private long to;

    @Client
    public SelectMessagePacket(long sessionId, long from, long to) {
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
    }

    @Auto
    @Server
    public SelectMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Server
    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        long current = this.from;
        int realSelected;

        MessageManager manager = Kalmia.SERVER.messageManager();

        long curSeq = manager.seq(this.sessionId);

        if (current > curSeq) {
            return;
        }

        List<Message> messages = ApricotCollectionFactor.arrayList(150);

        long to = Math.min(this.to,
                           curSeq
        );

        while (current < to) {
            long selected = Math.min(to - current,
                                     200
            );

            long endSelect = current + selected;

            manager.operation(this.sessionId,
                              current,
                              endSelect,
                              (seq, msg) -> {
                                  messages.add(msg);
                              }
            );

            realSelected = messages.size() - 1;

            router.send(new SelectedMessagePacket(this.sessionId,
                                                  current,
                                                  current + realSelected,
                                                  messages
            ));

            current += selected + 1;

            messages.clear();
        }
    }
}
