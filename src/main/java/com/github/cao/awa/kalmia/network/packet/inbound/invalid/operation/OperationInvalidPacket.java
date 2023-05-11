package com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.unsolve.invalid.operation.UnsolvedOperationInvalidPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

/**
 * @param <T>
 * @see UnsolvedOperationInvalidPacket
 */
public class OperationInvalidPacket<T extends PacketHandler<T>> extends Packet<T> {
    public static final byte[] ID = SkippedBase256.longToBuf(2147483647);

    private final String reason;

    public OperationInvalidPacket(String reason) {
        this.reason = reason;
    }

    public OperationInvalidPacket(BytesReader reader) {
        this(new String(reader.all(),
                        StandardCharsets.UTF_8
        ));
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(this.reason.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public byte[] id() {
        return ID;
    }

    @Override
    public void inbound(RequestRouter router, T handler) {
        System.out.println("Invalid packet because: " + this.reason);
    }
}