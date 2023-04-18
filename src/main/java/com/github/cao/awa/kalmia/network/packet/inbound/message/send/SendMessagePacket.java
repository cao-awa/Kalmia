package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.message.send.SendMessageRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.message.send.UnsolvedSendMessagePacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Arrays;

/**
 * @see SendMessageRequest
 * @see UnsolvedSendMessagePacket
 */
@Server
public class SendMessagePacket extends ReadonlyPacket<SolvedRequestHandler> {
    private final long sessionId;
    private final byte[] identity;
    private final byte[] msg;

    public SendMessagePacket(long sessionId, byte[] identity, byte[] msg) {
        this.sessionId = sessionId;
        this.identity = identity;
        this.msg = msg;
    }

    public static SendMessagePacket create(BytesReader reader) {
        return new SendMessagePacket(SkippedBase256.readLong(reader),
                                     reader.read(16),
                                     reader.all()
        );
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, SolvedRequestHandler handler) {
        System.out.println("UID: " + handler.getUid());
        System.out.println("SID: " + this.sessionId);
        System.out.println("IDT: " + Mathematics.radix(this.identity,
                                                       36
        ));
        System.out.println("MSG: " + Arrays.toString(this.msg));
    }
}
