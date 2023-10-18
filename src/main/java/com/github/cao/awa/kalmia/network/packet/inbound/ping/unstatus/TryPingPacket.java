package com.github.cao.awa.kalmia.network.packet.inbound.ping.unstatus;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see TryPingResponsePacket
 * @see UnsolvedTryPingPacket
 */
public class TryPingPacket extends PingPacket {
    @Client
    public TryPingPacket(long startTime, byte[] receipt) {
        super(startTime);
        receipt(receipt);
    }

    @Client
    public TryPingPacket(long startTime) {
        super(startTime);
        receipt(BytesRandomIdentifier.create(16));
    }

    @Auto
    @Server
    public TryPingPacket(BytesReader reader) {
        super(SkippedBase256.readLong(reader));
    }

    @Server
    @Override
    public void inbound(RequestRouter router, StatelessHandler handler) {
        router.send(new TryPingResponsePacket(startTime(),
                                              receipt()
        ));
    }

    public static final byte[] ID = SkippedBase256.longToBuf(4);

    @Override
    public byte[] id() {
        return ID;
    }
}
