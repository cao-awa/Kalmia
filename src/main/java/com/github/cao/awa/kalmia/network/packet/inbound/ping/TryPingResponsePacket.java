package com.github.cao.awa.kalmia.network.packet.inbound.ping;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.ping.PingHandler;
import com.github.cao.awa.kalmia.network.packet.request.ping.unstatus.TryPingResponseRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingResponsePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see TryPingResponseRequest
 * @see UnsolvedTryPingResponsePacket
 */
@Client
public class TryPingResponsePacket extends PingPacket {
    public TryPingResponsePacket(BytesReader reader) {
        super(SkippedBase256.readLong(reader));
    }

    @Override
    public void inbound(RequestRouter router, PingHandler handler) {
        double responseMillions = TimeUtil.processNano(startTime()) / 1000000D;
    }
}
