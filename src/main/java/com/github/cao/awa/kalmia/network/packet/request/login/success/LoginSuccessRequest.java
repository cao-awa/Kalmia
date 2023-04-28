package com.github.cao.awa.kalmia.network.packet.request.login.success;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.login.success.LoginSuccessPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see LoginSuccessPacket
 */
@Server
public class LoginSuccessRequest extends Request {
    public static final byte[] ID = SkippedBase256.longToBuf(9);
    private final long uid;
    private final byte[] token;

    public LoginSuccessRequest(long uid, byte[] token) {
        this.uid = uid;
        this.token = token;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.uid),
                                this.token
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
