package com.github.cao.awa.kalmia.network.packet.inbound.login.failed;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

@Generic
@AutoSolvedPacket(8)
public class LoginFailedPacket extends Packet<AuthedRequestHandler> {
    public static final byte[] ID = SkippedBase256.longToBuf(8);
    private final long uid;

    public LoginFailedPacket(long uid) {
        this.uid = uid;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.uid));
    }

    public LoginFailedPacket(BytesReader reader) {
        this.uid = SkippedBase256.readLong(reader);
    }

    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {
        System.out.println("---Login failed---");
        System.out.println("UID: " + this.uid);
    }
}
