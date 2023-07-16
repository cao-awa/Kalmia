package com.github.cao.awa.kalmia.network.packet.inbound.login.password;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.handler.inbound.disabled.DisabledRequestHandler;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.login.failed.LoginFailedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.success.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.DisabledUser;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Arrays;

@AutoSolvedPacket(100001)
public class LoginWithPasswordPacket extends Packet<StatelessHandler> {
    @AutoData
    private long uid;
    @AutoData
    private byte[] password;

    @Client
    public LoginWithPasswordPacket(long uid, byte[] password) {
        this.uid = uid;
        this.password = password;
    }

    @Auto
    @Server
    public LoginWithPasswordPacket(BytesReader reader) {
        super(reader);
    }

    public long getUid() {
        return this.uid;
    }

    public byte[] getPassword() {
        return this.password;
    }

    @Server
    @Override
    public void inbound(RequestRouter router, StatelessHandler handler) {
        // Start login here.
        User user = Kalmia.SERVER.userManager()
                                 .get(this.uid);

        if (user instanceof DefaultUser usr && usr.password()
                                                  .isSha() && Arrays.equals(usr.password()
                                                                               .password(),
                                                                            Mathematics.toBytes(MessageDigger.digest(this.password,
                                                                                                                     MessageDigger.Sha3.SHA_512
                                                                                                ),
                                                                                                16
                                                                            )
        )) {
            router.setStatus(RequestState.AUTHED);
            ((AuthedRequestHandler) router.getHandler()).setUid(this.uid);

            byte[] token = BytesRandomIdentifier.create(16);

            router.send(new LoginSuccessPacket(this.uid,
                                               token
            ));
        } else if (user instanceof DisabledUser disabled) {
            System.out.println("THIS ACCOUNT(" + this.uid + ") IS DISABLED");

            router.setStatus(RequestState.DISABLED);
            ((DisabledRequestHandler) router.getHandler()).setUid(this.uid);

            byte[] token = BytesRandomIdentifier.create(16);

            router.send(new LoginSuccessPacket(this.uid,
                                               token
            ));
        } else {
            router.send(new LoginFailedPacket(this.uid));
        }
    }
}
