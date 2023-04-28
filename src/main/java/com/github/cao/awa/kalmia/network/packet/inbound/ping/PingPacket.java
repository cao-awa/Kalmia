package com.github.cao.awa.kalmia.network.packet.inbound.ping;

import com.github.cao.awa.kalmia.network.handler.ping.PingHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;

/**
 * @see TryPingPacket
 * @see TryPingResponsePacket
 */
public abstract class PingPacket extends ReadonlyPacket<PingHandler> {
    private final long startTime;

    public PingPacket(long startTime) {
        this.startTime = startTime;
    }

    public long startTime() {
        return this.startTime;
    }
}
