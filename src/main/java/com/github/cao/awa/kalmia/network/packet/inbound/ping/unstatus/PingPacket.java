package com.github.cao.awa.kalmia.network.packet.inbound.ping.unstatus;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;

/**
 * @see TryPingPacket
 * @see TryPingResponsePacket
 */
public abstract class PingPacket extends Packet<StatelessHandler> {
    private final long startTime;

    public PingPacket(long startTime, byte[] receipt) {
        super(receipt);
        this.startTime = startTime;
    }

    @Override
    public byte[] payload() {
        return SkippedBase256.longToBuf(startTime());
    }

    public PingPacket(long startTime) {
        super(BytesRandomIdentifier.create(16));
        this.startTime = startTime;
    }

    public long startTime() {
        return this.startTime;
    }
}
