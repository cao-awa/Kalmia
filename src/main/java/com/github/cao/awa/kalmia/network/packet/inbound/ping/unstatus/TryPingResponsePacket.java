package com.github.cao.awa.kalmia.network.packet.inbound.ping.unstatus;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.ping.PingHandler;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingResponsePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see TryPingPacket
 * @see UnsolvedTryPingResponsePacket
 */
public class TryPingResponsePacket extends PingPacket {
    @Server
    public TryPingResponsePacket(long startTime, byte[] receipt) {
        super(startTime);
        receipt(receipt);
    }

    @Auto
    @Client
    public TryPingResponsePacket(BytesReader reader) {
        super(SkippedBase256.readLong(reader));
    }

    @Client
    @Override
    public void inbound(RequestRouter router, PingHandler handler) {
        double responseMillions = TimeUtil.processNano(startTime()) / 1000000D;
        System.out.println("Ping: " + responseMillions + "ms");
    }

    public static final byte[] ID = SkippedBase256.longToBuf(5);

    @Override
    public byte[] payload() {
        return SkippedBase256.longToBuf(startTime());
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
