package com.github.cao.awa.kalmia.network.packet.inbound.ping.unstatus;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.ping.PingHandler;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;

/**
 * @see TryPingResponsePacket
 * @see UnsolvedTryPingPacket
 */
@Generic
public class TryPingPacket extends PingPacket {
    public TryPingPacket(BytesReader reader) {
        super(SkippedBase256.readLong(reader));
    }

    @Override
    public void inbound(RequestRouter router, PingHandler handler) {
        router.send(new TryPingResponsePacket(startTime(),
                                              receipt()
        ));
    }

    public static final byte[] ID = SkippedBase256.longToBuf(4);

    public TryPingPacket(long startTime, byte[] receipt) {
        super(startTime);
        receipt(receipt);
    }

    public TryPingPacket(long startTime) {
        super(startTime);
        receipt(BytesRandomIdentifier.create(16));
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
