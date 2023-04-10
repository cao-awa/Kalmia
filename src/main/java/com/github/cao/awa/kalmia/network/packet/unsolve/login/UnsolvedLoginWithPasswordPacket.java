package com.github.cao.awa.kalmia.network.packet.unsolve.login;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.LoginWithPasswordPacket;
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
    public LoginWithPasswordPacket toPacket() {
        return new LoginWithPasswordPacket();
    }
}
