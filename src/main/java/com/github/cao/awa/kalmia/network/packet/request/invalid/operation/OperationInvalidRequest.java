package com.github.cao.awa.kalmia.network.packet.request.invalid.operation;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

/**
 * @see OperationInvalidPacket
 */
@Server
public class OperationInvalidRequest extends Request {
    public static final byte[] ID = SkippedBase256.longToBuf(2147483647);
    private final String reason;

    public OperationInvalidRequest(String reason) {
        this.reason = reason;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(this.reason.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
