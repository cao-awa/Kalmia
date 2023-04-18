package com.github.cao.awa.kalmia.network.packet.unsolve.login.failed;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.failed.LoginFailedPacket;

/**
 * @see LoginFailedPacket
 */
public class UnsolvedLoginFailedPacket extends UnsolvedPacket<LoginFailedPacket> {
    public UnsolvedLoginFailedPacket(byte[] data) {
        super(data);
    }

    @Override
    public LoginFailedPacket packet() {
        return LoginFailedPacket.create(reader());
    }
}
