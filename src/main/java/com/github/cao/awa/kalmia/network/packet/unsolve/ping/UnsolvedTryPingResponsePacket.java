package com.github.cao.awa.kalmia.network.packet.unsolve.ping;

import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingResponsePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see TryPingResponsePacket
 */
@Client
public class UnsolvedTryPingResponsePacket extends UnsolvedPingPacket<TryPingResponsePacket> {
    public UnsolvedTryPingResponsePacket(byte[] data) {
        super(data);
    }

    @Override
    public TryPingResponsePacket toPacket() {
        return TryPingResponsePacket.create(reader());
    }
}
