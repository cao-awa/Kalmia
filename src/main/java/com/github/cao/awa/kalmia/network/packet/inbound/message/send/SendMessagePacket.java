package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.message.PlainMessage;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Arrays;

@AutoSolvedPacket(10)
public class SendMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long sessionId;
    @AutoData
    private byte[] msg;

    @Auto
    @Server
    public SendMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Client
    public SendMessagePacket(long sessionId, byte[] msg, byte[] receipt) {
        super(receipt);
        this.sessionId = sessionId;
        this.msg = msg;
    }

    @Server
    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("UID: " + handler.getUid());
        System.out.println("SID: " + this.sessionId);
        System.out.println("IDT: " + Arrays.toString(receipt()));
        System.out.println("MSG: " + new String(this.msg));

        if (Kalmia.SERVER.sessionManager()
                         .accessible(handler.getUid(),
                                     this.sessionId
                         )) {
            long seq = Kalmia.SERVER.messageManager()
                                    .send(
                                            this.sessionId,
                                            PlainMessage.create(this.msg)
                                    );

            // Response to client the seq.
            router.send(new SentMessagePacket(seq,
                                              receipt()
            ));
        } else {
            // Unable to access the session.
            System.out.println("www");
        }
    }
}
