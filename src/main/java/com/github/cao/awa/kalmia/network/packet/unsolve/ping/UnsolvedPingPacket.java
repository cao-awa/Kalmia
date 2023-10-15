package com.github.cao.awa.kalmia.network.packet.unsolve.ping;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.unstatus.PingPacket;

public abstract class UnsolvedPingPacket<T extends PingPacket> extends UnsolvedPacket<T> {
    public UnsolvedPingPacket(byte[] data) {
        super(data);
    }

    public boolean isStateless() {
        return true;
    }
}
