package com.github.cao.awa.kalmia.network.packet.request.login.failed;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.failed.LoginFailedPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see LoginFailedPacket
 */
@Server
public class LoginFailedRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(8);
    private final long uid;

    public LoginFailedRequest(long uid) {
        this.uid = uid;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.uid));
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
