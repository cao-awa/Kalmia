package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.manage.MessageManager;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.util.List;

@Generic
@AutoSolvedPacket(12)
public class SelectMessagePacket extends Packet<AuthedRequestHandler> {
    private final long sessionId;
    private final long from;
    private final long to;

    public SelectMessagePacket(BytesReader reader) {
        this.sessionId = SkippedBase256.readLong(reader);
        this.from = SkippedBase256.readLong(reader);
        this.to = SkippedBase256.readLong(reader);
    }

    public SelectMessagePacket(long sessionId, long from, long to) {
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                SkippedBase256.longToBuf(this.from),
                                SkippedBase256.longToBuf(this.to)
        );
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        long current = this.from;
        int realSelected;

        MessageManager manager = Kalmia.SERVER.messageManager();

        long curSeq = manager.seq(this.sessionId);

        if (current > curSeq) {
            return;
        }

        List<Message> messages = ApricotCollectionFactor.newArrayList(150);

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
