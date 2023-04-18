package com.github.cao.awa.kalmia.network.packet.request.invalid.operation;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see OperationInvalidPacket
 */
@Server
public class OperationInvalidRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(2147483647);

    @Override
    public byte[] data() {
        return new byte[0];
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
