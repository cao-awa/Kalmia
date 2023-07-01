package com.github.cao.awa.kalmia.network.packet.inbound.disconnet;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AutoSolvedPacket(114514)
public class DisconnectPacket extends Packet<StatelessHandler> {
    private static final Logger LOGGER = LogManager.getLogger("DisconnectPacket");
    @AutoData
    public String reason;

    public DisconnectPacket(String reason) {
        this.reason = reason;
    }

    @Auto
    public DisconnectPacket(BytesReader reader) {
        super(reader);
    }

    @Override
    public void inbound(RequestRouter router, StatelessHandler handler) {
        LOGGER.info("Disconnecting from request");
        router.disconnect();
    }
}
