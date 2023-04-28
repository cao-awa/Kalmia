package com.github.cao.awa.kalmia.network.packet.request.account.function;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.account.function.DisableInstancePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see DisableInstancePacket
 */
@Client
public class DisableInstanceRequest extends Request {
    private static final byte[] ID = SkippedBase256.longToBuf(68943);

    @Override
    public byte[] data() {
        return new byte[0];
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
