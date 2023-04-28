package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.message.send.SentMessageRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see SentMessageRequest
 */
@Client
@AutoSolvedPacket(11)
public class SentMessagePacket extends ReadonlyPacket<AuthedRequestHandler> {
    private final long seq;

    public SentMessagePacket(BytesReader reader) {
        this.seq = SkippedBase256.readLong(reader);
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
