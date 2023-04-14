package com.github.cao.awa.kalmia.network.packet.request.ping.unstatus;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingResponsePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see TryPingResponsePacket
 */
@Server
public class TryPingResponseRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(- 2);

    private final long startTime;

    public TryPingResponseRequest(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public byte[] data() {
        return SkippedBase256.longToBuf(this.startTime);
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
