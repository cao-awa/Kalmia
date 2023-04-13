package com.github.cao.awa.kalmia.network.packet.unsolve.handshake;

import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;

public abstract class UnsolvedHandshakePacket<T extends ReadonlyPacket<?>> extends UnsolvedPacket<T> {
    public UnsolvedHandshakePacket(byte[] data) {
        super(data);
    }
}
