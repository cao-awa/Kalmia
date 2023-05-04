package com.github.cao.awa.kalmia.network.packet.dual.message.delete;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.dual.DualPacket;
import com.github.cao.awa.kalmia.network.packet.dual.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

@Generic
@AutoSolvedPacket(15)
public class DeletedMessagePacket extends DualPacket<AuthedRequestHandler> {
    private final long sid;
    private final long seq;

    public DeletedMessagePacket(BytesReader reader) {
        this.sid = SkippedBase256.readLong(reader);
        this.seq = SkippedBase256.readLong(reader);
    }

    public DeletedMessagePacket(long sid, long seq) {
        this.sid = sid;
        this.seq = seq;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sid),
                                SkippedBase256.longToBuf(this.seq)
        );
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("---Message deleted---");
        System.out.println("UID: " + handler.getUid());
        System.out.println("SID: " + this.sid);
        System.out.println("SEQ: " + this.seq);

        router.send(new SelectMessagePacket(123,
                                            0,
                                            114514
        ));
    }
}
