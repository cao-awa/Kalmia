package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;

@Generic
@AutoSolvedPacket(11)
public class SentMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long seq;

    public SentMessagePacket(BytesReader reader) {
        super(reader);
    }

    public SentMessagePacket(long seq, byte[] receipt) {
        super(receipt);
        this.seq = seq;
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("UID: " + handler.getUid());
        System.out.println("IDT: " + Mathematics.radix(receipt(),
                                                       36
        ));
        System.out.println("SEQ: " + this.seq);
    }
}
