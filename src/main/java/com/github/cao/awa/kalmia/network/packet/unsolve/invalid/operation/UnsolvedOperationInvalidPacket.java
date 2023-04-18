package com.github.cao.awa.kalmia.network.packet.unsolve.invalid.operation;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;

public class UnsolvedOperationInvalidPacket extends UnsolvedPacket<OperationInvalidPacket<?>> {
    public UnsolvedOperationInvalidPacket(byte[] data) {
        super(data);
    }

    @Override
    public OperationInvalidPacket<?> packet() {
        return new OperationInvalidPacket<>();
    }
}
