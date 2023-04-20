package com.github.cao.awa.kalmia.network.packet.inbound.login.failed;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.login.failed.LoginFailedRequest;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see LoginFailedRequest
 */
@Client
@AutoSolvedPacket(8)
public class LoginFailedPacket extends ReadonlyPacket<SolvedRequestHandler> {
    private final long uid;

    public LoginFailedPacket(BytesReader reader) {
        this.uid = SkippedBase256.readLong(reader);
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, SolvedRequestHandler handler) {
        System.out.println("---Login failed---");
        System.out.println("UID: " + this.uid);
    }
}
