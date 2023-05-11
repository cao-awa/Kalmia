package com.github.cao.awa.kalmia.network.packet.inbound.message.delete;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

@Generic
@AutoSolvedPacket(14)
public class DeleteMessagePacket extends Packet<AuthedRequestHandler> {
    private final long sessionId;
    private final long seq;

    public DeleteMessagePacket(BytesReader reader) {
        this.sessionId = SkippedBase256.readLong(reader);
        this.seq = SkippedBase256.readLong(reader);
    }

    public DeleteMessagePacket(long sessionId, long seq) {
        this.sessionId = sessionId;
        this.seq = seq;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                SkippedBase256.longToBuf(this.seq)
        );
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("UID: " + handler.getUid());
        System.out.println("SID: " + this.sessionId);
        System.out.println("SEQ: " + this.seq);

        Kalmia.SERVER.messageManager()
                     .delete(this.sessionId,
                             this.seq
                     );

        // Response to client the seq.
        router.send(new DeletedMessagePacket(this.sessionId,
                                             this.seq
        ));

//        Kalmia.SERVER.messageManager().operation(123, (s, m) -> {
//            System.out.println("---");
//            if (m instanceof PlainMessage plain) {
//                System.out.println(s + ": " + plain.getMsg());
//            } else {
//                System.out.println(s + ": " + "<DELETED>");
//            }
//        });
    }
}
