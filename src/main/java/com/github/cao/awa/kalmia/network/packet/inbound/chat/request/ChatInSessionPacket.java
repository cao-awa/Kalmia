package com.github.cao.awa.kalmia.network.packet.inbound.chat.request;

import com.github.cao.awa.apricot.anntation.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(17)
public class ChatInSessionPacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long targetUid;
    @AutoData
    private long sessionId;

    @Server
    public ChatInSessionPacket(long targetUid, long sessionId) {
        this.targetUid = targetUid;
        this.sessionId = sessionId;
    }

    @Auto
    @Client
    public ChatInSessionPacket(BytesReader reader) {
        super(reader);
    }

    @Client
    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("Requested session for " + this.targetUid + ": " + this.sessionId);
    }
}
