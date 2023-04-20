package com.github.cao.awa.kalmia.network.packet.inbound.login.success;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.login.success.LoginSuccessRequest;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see LoginSuccessRequest
 */
@Client
@AutoSolvedPacket(9)
public class LoginSuccessPacket extends ReadonlyPacket<SolvedRequestHandler> {
    private final long uid;
    private final byte[] token;

    public LoginSuccessPacket(BytesReader reader) {
        this.uid = SkippedBase256.readLong(reader);
        this.token = reader.all();
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, SolvedRequestHandler handler) {
        System.out.println("---Login success---");
        System.out.println("UID: " + this.uid);
        System.out.println("Token: " + Mathematics.radix(this.token,
                                                         36
        ));
    }
}
