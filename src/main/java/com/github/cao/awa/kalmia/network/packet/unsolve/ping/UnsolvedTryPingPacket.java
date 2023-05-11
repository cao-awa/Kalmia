package com.github.cao.awa.kalmia.network.packet.unsolve.ping;

import com.github.cao.awa.kalmia.network.packet.inbound.ping.unstatus.TryPingPacket;
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
        return new TryPingPacket(reader()).receipt(receipt());
    }
}
