package com.github.cao.awa.kalmia.network.packet.inbound.message.delete;

import com.github.cao.awa.apricot.anntation.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(14)
public class DeleteMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long sessionId;
    @AutoData
    private long seq;

    @Auto
    @Server
    public DeleteMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Client
    public DeleteMessagePacket(long sessionId, long seq) {
        this.sessionId = sessionId;
        this.seq = seq;
    }

    @Server
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
