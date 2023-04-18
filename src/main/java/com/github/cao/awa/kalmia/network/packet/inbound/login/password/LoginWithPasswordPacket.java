package com.github.cao.awa.kalmia.network.packet.inbound.login.password;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.network.login.password.LoginWithPasswordEvent;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.handler.login.LoginHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.login.failed.LoginFailedRequest;
import com.github.cao.awa.kalmia.network.packet.request.login.password.LoginWithPasswordRequest;
import com.github.cao.awa.kalmia.network.packet.request.login.success.LoginSuccessRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.login.password.UnsolvedLoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Arrays;

/**
 * @see LoginWithPasswordRequest
 * @see UnsolvedLoginWithPasswordPacket
 */
@Server
public class LoginWithPasswordPacket extends ReadonlyPacket<LoginHandler> {
    private final long uid;
    private final byte[] password;

    public LoginWithPasswordPacket(long uid, byte[] password) {
        this.uid = uid;
        this.password = password;
    }

    public static LoginWithPasswordPacket create(BytesReader reader) {
        return new LoginWithPasswordPacket(SkippedBase256.readLong(reader),
                                           reader.read(reader.read())
        );
    }

    public long getUid() {
        return this.uid;
    }

    public byte[] getPassword() {
        return this.password;
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, LoginHandler handler) {
        // Trigger the pre handlers.
        LoginWithPasswordEvent.trigger(this,
                                       router,
                                       handler
        );

        // Start login here.
        User user = Kalmia.SERVER.userManager()
                                 .get(this.uid);

        if (user instanceof DefaultUser usr && usr.getPassword()
                                                  .isSha() && Arrays.equals(usr.getPassword()
                                                                               .password(),
                                                                            Mathematics.toBytes(MessageDigger.digest(this.password,
                                                                                                                     MessageDigger.Sha3.SHA_512
                                                                                                ),
                                                                                                16
                                                                            )
        )) {
            router.setStatus(RequestStatus.AUTHED);
            ((SolvedRequestHandler) router.getHandler()).setUid(this.uid);

            byte[] token = BytesRandomIdentifier.create(16);

            router.send(new LoginSuccessRequest(this.uid,
                                                token
            ));
        } else {
            router.send(new LoginFailedRequest(this.uid));
        }
    }
}
