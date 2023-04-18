package com.github.cao.awa.kalmia.network.packet.unsolve.login.success;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.success.LoginSuccessPacket;

/**
 * @see LoginSuccessPacket
 */
public class UnsolvedLoginSuccessPacket extends UnsolvedPacket<LoginSuccessPacket> {
    public UnsolvedLoginSuccessPacket(byte[] data) {
        super(data);
    }

    @Override
    public LoginSuccessPacket packet() {
        return LoginSuccessPacket.create(reader());
    }
}
