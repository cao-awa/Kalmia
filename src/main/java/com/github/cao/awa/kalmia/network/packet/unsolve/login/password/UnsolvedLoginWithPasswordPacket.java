package com.github.cao.awa.kalmia.network.packet.unsolve.login.password;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see LoginWithPasswordPacket
 */
@Server
public class UnsolvedLoginWithPasswordPacket extends UnsolvedPacket<LoginWithPasswordPacket> {
    public UnsolvedLoginWithPasswordPacket(byte[] data) {
        super(data);
    }

    @Override
    public LoginWithPasswordPacket packet() {
        return LoginWithPasswordPacket.create(reader());
    }
}
