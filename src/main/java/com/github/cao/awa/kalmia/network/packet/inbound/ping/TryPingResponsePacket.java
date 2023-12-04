package com.github.cao.awa.kalmia.network.packet.inbound.ping;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.ping.TryPingResponseEvent;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingResponsePacket;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see TryPingPacket
 * @see UnsolvedTryPingResponsePacket
 */
@NetworkEventTarget(TryPingResponseEvent.class)
public class TryPingResponsePacket extends PingPacket {
    public static final byte[] ID = SkippedBase256.longToBuf(5);
    private final long responseStartTime;

    public TryPingResponsePacket(long startTime, byte[] receipt) {
        super(startTime);
        receipt(receipt);
        responseStartTime = TimeUtil.millions();
    }

    @Auto
    public TryPingResponsePacket(BytesReader reader) {
        super(SkippedBase256.readLong(reader));
        this.responseStartTime = SkippedBase256.readLong(reader);
    }

    public long responseStartTime() {
        return this.responseStartTime;
    }

    @Override
    public byte[] payload() {
        return BytesUtil.concat(
                SkippedBase256.longToBuf(startTime()),
                SkippedBase256.longToBuf(responseStartTime())
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
