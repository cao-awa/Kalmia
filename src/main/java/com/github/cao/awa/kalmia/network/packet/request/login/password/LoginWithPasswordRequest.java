package com.github.cao.awa.kalmia.network.packet.request.login.password;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see LoginWithPasswordPacket
 */
@Client
public class LoginWithPasswordRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(6);
    private final long uid;

    public LoginWithPasswordRequest(long uid) {
        this.uid = uid;
    }

    @Override
    public byte[] data() {
        return SkippedBase256.longToBuf(this.uid);
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
