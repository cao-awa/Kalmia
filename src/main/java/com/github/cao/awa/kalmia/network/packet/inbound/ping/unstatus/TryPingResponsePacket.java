package com.github.cao.awa.kalmia.network.packet.inbound.ping.unstatus;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.ping.PingHandler;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingResponsePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;

/**
 * @see TryPingPacket
 * @see UnsolvedTryPingResponsePacket
 */
@Generic
public class TryPingResponsePacket extends PingPacket {
    public TryPingResponsePacket(BytesReader reader) {
        super(SkippedBase256.readLong(reader));
    }

    @Override
    public void inbound(RequestRouter router, PingHandler handler) {
        double responseMillions = TimeUtil.processNano(startTime()) / 1000000D;
        System.out.println("Ping: " + responseMillions + "ms");
    }

    public static final byte[] ID = SkippedBase256.longToBuf(5);

    public TryPingResponsePacket(long startTime, byte[] receipt) {
        super(startTime);
        receipt(receipt);
    }

    @Override
    public byte[] data() {
        return SkippedBase256.longToBuf(startTime());
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
