package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.anntation.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;

@AutoSolvedPacket(13)
public class SelectedMessagePacket extends Packet<AuthedRequestHandler> {
    private final long sessionId;
    private final long from;
    private final long to;
    private final List<Message> messages;

    @Server
    public SelectedMessagePacket(long sessionId, long from, long to, List<Message> messages) {
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
        this.messages = messages;
    }

    @Auto
    @Client
    public SelectedMessagePacket(BytesReader reader) {
        this.sessionId = SkippedBase256.readLong(reader);
        this.from = SkippedBase256.readLong(reader);
        this.to = SkippedBase256.readLong(reader);
        this.messages = ApricotCollectionFactor.newArrayList();
        while (reader.readable(1)) {
            this.messages.add(Message.create(reader));
        }
    }

    @Client
    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("Received msg of session " + this.sessionId);
        System.out.println("Range is " + this.from + " to " + this.to);

//        for (Message message : this.messages) {
//            if (message instanceof PlainMessage plain) {
//                System.out.println("PLAINS: " + plain.getMsg());
//            } else if (message instanceof DeletedMessage deleted) {
//                System.out.println("DELETED: " + deleted.getDigestData()
//                                                        .value36());
//            }
//        }
    }

    @Override
    public byte[] payload() {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            for (Message msg : this.messages) {
                if (msg == null) {
                    continue;
                }
                output.write(msg.toBytes());
            }
            return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                    SkippedBase256.longToBuf(this.from),
                                    SkippedBase256.longToBuf(this.to),
                                    output.toByteArray()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
