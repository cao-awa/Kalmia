package com.github.cao.awa.kalmia.network.packet.inbound.chat.request;

import com.github.cao.awa.apricot.anntation.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.session.duet.DuetSession;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(16)
public class RequestDuetSessionPacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long targetUid;

    @Client
    public RequestDuetSessionPacket(long targetUid) {
        this.targetUid = targetUid;
    }

    @Auto
    @Server
    public RequestDuetSessionPacket(BytesReader reader) {
        super(reader);
    }

    @Server
    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        long sessionId = Kalmia.SERVER.userManager()
                                      .session(handler.getUid(),
                                               this.targetUid
                                      );
        if (sessionId == - 1) {
            sessionId = Kalmia.SERVER.sessionManager()
                                     .add(new DuetSession(handler.getUid(),
                                                          this.targetUid
                                     ));
            Kalmia.SERVER.userManager()
                         .session(handler.getUid(),
                                  this.targetUid,
                                  sessionId
                         );
        }

        router.send(new ChatInSessionPacket(this.targetUid,
                                            sessionId
        ));
    }
}
