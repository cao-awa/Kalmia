package com.github.cao.awa.kalmia.network.packet.request.login;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.LoginWithPasswordPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see LoginWithPasswordPacket
 */
@Client
public class LoginWithPasswordRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(6);

    @Override
    public byte[] data() {
        return new byte[0];
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
