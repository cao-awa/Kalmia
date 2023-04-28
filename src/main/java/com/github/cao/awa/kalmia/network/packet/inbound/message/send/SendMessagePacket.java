package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.PlainMessage;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.message.send.SendMessageRequest;
import com.github.cao.awa.kalmia.network.packet.request.message.send.SentMessageRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Arrays;

/**
 * @see SendMessageRequest
 */
@Server
@AutoSolvedPacket(10)
public class SendMessagePacket extends ReadonlyPacket<AuthedRequestHandler> {
    private final long sessionId;
    private final byte[] msg;

    public SendMessagePacket(BytesReader reader) {
        this.sessionId = SkippedBase256.readLong(reader);
        this.msg = reader.all();
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
        router.send(new SentMessageRequest(seq,
                                           receipt()
        ));
    }
}
