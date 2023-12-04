package com.github.cao.awa.kalmia.network.packet.inbound.test;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AutoSolvedPacket(id = Integer.MAX_VALUE - 1)
public class TestPacket extends Packet<HandshakeHandler> {
    private static final Logger LOGGER = LogManager.getLogger("TestPacket");
    @AutoData
    private RequestProtocol majorProtocol;
    @AutoData
    private String clientVersion;

    @Client
    public TestPacket(RequestProtocol majorProtocol, String clientVersion) {
        this.majorProtocol = majorProtocol;
        this.clientVersion = clientVersion;
    }

    @Auto
    @Server
    public TestPacket(BytesReader reader) {
        super(reader);
    }

    public void inbound(RequestRouter router, HandshakeHandler handler) {
        LOGGER.info("Client connect using major protocol " + majorProtocol()
                .name() + " version " + majorProtocol()
                .version() + " by client: " + clientVersion());
    }

    public RequestProtocol majorProtocol() {
        return this.majorProtocol;
    }

    public String clientVersion() {
        return this.clientVersion;
    }
}
