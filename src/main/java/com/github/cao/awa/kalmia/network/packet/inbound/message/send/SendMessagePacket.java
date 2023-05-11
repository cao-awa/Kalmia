package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.PlainMessage;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.util.Arrays;

@Generic
@AutoSolvedPacket(10)
public class SendMessagePacket extends Packet<AuthedRequestHandler> {
    private final long sessionId;
    private final byte[] msg;

    public SendMessagePacket(BytesReader reader) {
        this.sessionId = SkippedBase256.readLong(reader);
        this.msg = reader.all();
    }

    public SendMessagePacket(long sessionId, byte[] msg, byte[] receipt) {
        super(receipt);
        this.sessionId = sessionId;
        this.msg = msg;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                this.msg
        );
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("UID: " + handler.getUid());
        System.out.println("SID: " + this.sessionId);
        System.out.println("IDT: " + Mathematics.radix(receipt(),
                                                       36
        ));
        System.out.println("MSG: " + Arrays.toString(this.msg));

        long seq = Kalmia.SERVER.messageManager()
                                .send(this.sessionId,
                                      PlainMessage.create(this.msg)
                                );

        // Response to client the seq.
        router.send(new SentMessagePacket(seq,
                                          receipt()
        ));
    }
}
