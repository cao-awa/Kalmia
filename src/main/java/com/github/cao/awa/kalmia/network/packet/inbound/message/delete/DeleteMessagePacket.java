package com.github.cao.awa.kalmia.network.packet.inbound.message.delete;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.message.delete.DeleteMessageRequest;
import com.github.cao.awa.kalmia.network.packet.request.message.delete.DeletedMessageRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see DeleteMessageRequest
 */
@Server
@AutoSolvedPacket(14)
public class DeleteMessagePacket extends ReadonlyPacket<AuthedRequestHandler> {
    private final long sessionId;
    private final long seq;

    public DeleteMessagePacket(BytesReader reader) {
        this.sessionId = SkippedBase256.readLong(reader);
        this.seq = SkippedBase256.readLong(reader);
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
        router.send(new DeletedMessageRequest(this.sessionId,
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
