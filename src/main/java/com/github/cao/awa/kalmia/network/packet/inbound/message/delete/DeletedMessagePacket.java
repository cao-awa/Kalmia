package com.github.cao.awa.kalmia.network.packet.inbound.message.delete;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;

@Generic
@AutoSolvedPacket(15)
public class DeletedMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long sid;
    @AutoData
    private long seq;

    public DeletedMessagePacket(BytesReader reader) {
        super(reader);
    }

    public DeletedMessagePacket(long sid, long seq) {
        this.sid = sid;
        this.seq = seq;
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
