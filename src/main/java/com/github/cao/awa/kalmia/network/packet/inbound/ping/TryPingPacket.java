package com.github.cao.awa.kalmia.network.packet.inbound.ping;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.ping.PingHandler;
import com.github.cao.awa.kalmia.network.packet.request.ping.unstatus.TryPingRequest;
import com.github.cao.awa.kalmia.network.packet.request.ping.unstatus.TryPingResponseRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see TryPingRequest
 * @see UnsolvedTryPingPacket
 */
@Server
public class TryPingPacket extends PingPacket {
    public TryPingPacket(BytesReader reader) {
        super(SkippedBase256.readLong(reader));
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, PingHandler handler) {
        router.send(new TryPingResponseRequest(startTime()));
    }
}
