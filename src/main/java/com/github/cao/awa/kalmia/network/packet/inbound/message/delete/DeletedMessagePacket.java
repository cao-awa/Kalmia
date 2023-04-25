package com.github.cao.awa.kalmia.network.packet.inbound.message.delete;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.message.delete.DeletedMessageRequest;
import com.github.cao.awa.kalmia.network.packet.request.message.select.SelectMessageRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see DeletedMessageRequest
 */
@Client
@AutoSolvedPacket(15)
public class DeletedMessagePacket extends ReadonlyPacket<AuthedRequestHandler> {
    private final long sid;
    private final long seq;

    public DeletedMessagePacket(BytesReader reader) {
        this.sid = SkippedBase256.readLong(reader);
        this.seq = SkippedBase256.readLong(reader);
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("---Message deleted---");
        System.out.println("UID: " + handler.getUid());
        System.out.println("SID: " + this.sid);
        System.out.println("SEQ: " + this.seq);

        router.send(new SelectMessageRequest(123,
                                             0,
                                             114514
        ));
    }
}
