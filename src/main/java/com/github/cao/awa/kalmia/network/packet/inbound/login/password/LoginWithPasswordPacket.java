package com.github.cao.awa.kalmia.network.packet.inbound.login.password;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.handler.inbound.disabled.DisabledRequestHandler;
import com.github.cao.awa.kalmia.network.handler.login.LoginHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.login.failed.LoginFailedRequest;
import com.github.cao.awa.kalmia.network.packet.request.login.password.LoginWithPasswordRequest;
import com.github.cao.awa.kalmia.network.packet.request.login.success.LoginSuccessRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.DisabledUser;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Arrays;

/**
 * @see LoginWithPasswordRequest
 */
@Server
@AutoSolvedPacket(6)
public class LoginWithPasswordPacket extends ReadonlyPacket<LoginHandler> {
    private final long uid;
    private final byte[] password;

    public LoginWithPasswordPacket(BytesReader reader) {
        this.uid = SkippedBase256.readLong(reader);
        this.password = reader.read(reader.read());
    }

    public long getUid() {
        return this.uid;
    }

    public byte[] getPassword() {
        return this.password;
    }

    @Override
    public void inbound(RequestRouter router, LoginHandler handler) {
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
            ((AuthedRequestHandler) router.getHandler()).setUid(this.uid);

            byte[] token = BytesRandomIdentifier.create(16);

            router.send(new LoginSuccessRequest(this.uid,
                                                token
            ));
        } else if (user instanceof DisabledUser disabled) {
            System.out.println("THIS ACCOUNT(" + this.uid + ") IS DISABLED");

            router.setStatus(RequestStatus.DISABLED);
            ((DisabledRequestHandler) router.getHandler()).setUid(this.uid);

            byte[] token = BytesRandomIdentifier.create(16);

            router.send(new LoginSuccessRequest(this.uid,
                                                token
            ));
        } else {
            // TODO Test only
            Kalmia.SERVER.userManager()
                         .set(123456,
                              new DefaultUser(TimeUtil.nano(),
                                              "awa".getBytes()
                              )
                         );
            router.send(new LoginFailedRequest(this.uid));
        }
    }
}
