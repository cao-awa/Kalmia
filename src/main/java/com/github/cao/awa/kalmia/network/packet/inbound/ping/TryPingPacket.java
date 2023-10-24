package com.github.cao.awa.kalmia.network.packet.inbound.ping;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.annotation.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.ping.TryPingEvent;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingPacket;

/**
 * @see TryPingResponsePacket
 * @see UnsolvedTryPingPacket
 */
@NetworkEventTarget(TryPingEvent.class)
public class TryPingPacket extends PingPacket {
    public static final byte[] ID = SkippedBase256.longToBuf(4);

    public TryPingPacket(byte[] receipt) {
        super(TimeUtil.nano());
        receipt(receipt);
    }

    public TryPingPacket() {
        super(TimeUtil.nano());
        receipt(BytesRandomIdentifier.create(16));
    }

    @Auto
    public TryPingPacket(BytesReader reader) {
        super(SkippedBase256.readLong(reader));
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
