package com.github.cao.awa.kalmia.network.packet.inbound.login.failed;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;

@Generic
@AutoSolvedPacket(8)
public class LoginFailedPacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long uid;

    public LoginFailedPacket(long uid) {
        this.uid = uid;
    }

    public LoginFailedPacket(BytesReader reader) {
        super(reader);
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("---Login failed---");
        System.out.println("UID: " + this.uid);
    }
}
