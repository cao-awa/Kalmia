package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.PlainMessage;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.message.select.SelectedMessageRequest;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

import java.util.List;

/**
 * @see SelectedMessageRequest
 */
@Client
@AutoSolvedPacket(13)
public class SelectedMessagePacket extends ReadonlyPacket<SolvedRequestHandler> {
    private final long sessionId;
    private final long from;
    private final long to;
    private final List<Message> messages;

    public SelectedMessagePacket(BytesReader reader) {
        this.sessionId = SkippedBase256.readLong(reader);
        this.from = SkippedBase256.readLong(reader);
        this.to = SkippedBase256.readLong(reader);
        this.messages = ApricotCollectionFactor.newArrayList();
        while (reader.readable(1)) {
            this.messages.add(Message.create(reader));
        }
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, SolvedRequestHandler handler) {
        System.out.println("Received msg of session " + this.sessionId);
        System.out.println("Range is " + this.from + " to " + this.to);

        for (Message message : this.messages) {
            if (message instanceof PlainMessage plain) {
                System.out.println("PLAINS: " + plain.getMsg());
            } else if (message instanceof DeletedMessage deleted) {
                System.out.println("DELETED: " + deleted.getDigest());
            }
        }
    }
}
