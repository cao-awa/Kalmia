package com.github.cao.awa.kalmia.network.packet.unsolve.ping;

import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see TryPingPacket
 */
@Server
public class UnsolvedTryPingPacket extends UnsolvedPingPacket<TryPingPacket> {
    public UnsolvedTryPingPacket(byte[] data) {
        super(data);
    }

    @Override
    public TryPingPacket packet() {
        return TryPingPacket.create(reader());
    }
}
