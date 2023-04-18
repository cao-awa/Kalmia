package com.github.cao.awa.kalmia.network.packet.inbound.login.password;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.handler.login.LoginHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.login.password.LoginWithPasswordRequest;
import com.github.cao.awa.kalmia.network.packet.request.login.success.LoginSuccessRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.login.password.UnsolvedLoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see LoginWithPasswordRequest
 * @see UnsolvedLoginWithPasswordPacket
 */
@Server
public class LoginWithPasswordPacket extends ReadonlyPacket<LoginHandler> {
    private final long uid;

    public LoginWithPasswordPacket(long uid) {
        this.uid = uid;
    }

    public static LoginWithPasswordPacket create(BytesReader reader) {
        return new LoginWithPasswordPacket(SkippedBase256.readLong(reader));
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, LoginHandler handler) {
        System.out.println("Client Login");
        router.setStatus(RequestStatus.AUTHED);
        ((SolvedRequestHandler) router.getHandler()).setUid(this.uid);

        byte[] token = BytesRandomIdentifier.create(16);

        router.send(new LoginSuccessRequest(this.uid,
                                            token
        ));

        System.out.println("Token: " + Mathematics.radix(token,
                                                         36
        ));

//        router.send(new LoginFailedRequest(this.uid));
    }
}
