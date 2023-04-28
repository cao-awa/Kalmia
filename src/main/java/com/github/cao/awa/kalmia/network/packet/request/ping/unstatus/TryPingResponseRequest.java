package com.github.cao.awa.kalmia.network.packet.request.ping.unstatus;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.ReceiptRequest;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingResponsePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see TryPingResponsePacket
 */
@Server
public class TryPingResponseRequest extends ReceiptRequest {
    public static final byte[] ID = SkippedBase256.longToBuf(5);
    private final long startTime;

    public TryPingResponseRequest(long startTime, byte[] receipt) {
        super(receipt);
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
