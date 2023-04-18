package com.github.cao.awa.kalmia.network.packet.request.login.password;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see LoginWithPasswordPacket
 */
@Client
public class LoginWithPasswordRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(6);
    private final long uid;
    private final byte[] password;

    public LoginWithPasswordRequest(long uid, byte[] password) {
        this.uid = uid;
        this.password = password;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.uid),
                                new byte[]{(byte) this.password.length},
                                this.password
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
