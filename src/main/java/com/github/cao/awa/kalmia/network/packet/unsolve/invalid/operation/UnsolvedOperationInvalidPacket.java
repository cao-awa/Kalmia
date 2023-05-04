package com.github.cao.awa.kalmia.network.packet.unsolve.invalid.operation;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.dual.invalid.operation.OperationInvalidPacket;

import java.nio.charset.StandardCharsets;

public class UnsolvedOperationInvalidPacket extends UnsolvedPacket<OperationInvalidPacket<?>> {
    public UnsolvedOperationInvalidPacket(byte[] data) {
        super(data);
    }

    @Override
    public OperationInvalidPacket<?> packet() {
        return new OperationInvalidPacket<>(new String(reader().all(),
                                                       StandardCharsets.UTF_8
        ));
    }
}
