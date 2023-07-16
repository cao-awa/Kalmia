package com.github.cao.awa.kalmia.network.packet.inbound.login.sign;

import com.github.cao.awa.apricot.annotation.Unsupported;
import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.user.pubkey.PublicKeyIdentity;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Unsupported
@AutoSolvedPacket(100003)
public class LoginWithSignPacket extends Packet<StatelessHandler> {
    @AutoData
    private long uid;

    @Client
    public LoginWithSignPacket(int uid) {
        this.uid = uid;
    }

    @Auto
    @Server
    public LoginWithSignPacket(BytesReader reader) {
        super(reader);
    }

    @Server
    @Override
    public void inbound(RequestRouter router, StatelessHandler handler) {
        if (! PublicKeyIdentity.ensurePublicKeySettings(this.uid,
                                                        router
        )) {
            return;
        }
        Kalmia.SERVER.userManager()
                     .publicKey(this.uid);
    }
}
