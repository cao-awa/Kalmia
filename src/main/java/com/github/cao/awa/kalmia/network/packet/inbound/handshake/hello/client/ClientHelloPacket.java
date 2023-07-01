package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaPreSharedKey;
import com.github.cao.awa.kalmia.network.encode.crypto.asymmetric.ac.EcCrypto;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.DisconnectPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

@AutoSolvedPacket(0)
public class ClientHelloPacket extends Packet<HandshakeHandler> {
    private static final Logger LOGGER = LogManager.getLogger("ClientHelloPacket");
    private static final boolean SHOULD_RSA = true;
    private static final boolean SHOULD_SYM = true;
    private static final long SYM_ID = - 1;
    private final RequestProtocol majorProtocol;
    private final String clientVersion;
    private final String expectCipherKey;

    @Client
    public ClientHelloPacket(RequestProtocol majorProtocol, String clientVersion, String expectCipherKey) {
        this.majorProtocol = majorProtocol;
        this.clientVersion = clientVersion;
        this.expectCipherKey = expectCipherKey;
    }

    @Client
    public ClientHelloPacket(RequestProtocol majorProtocol, String clientVersion) {
        this(majorProtocol,
             clientVersion,
             KalmiaPreSharedKey.expectCipherKey
        );
    }

    @Auto
    @Server
    public ClientHelloPacket(BytesReader reader) {
        this(RequestProtocol.create(reader),
             new String(reader.read(reader.read()),
                        StandardCharsets.UTF_8
             ),
             new String(reader.read(reader.read()),
                        StandardCharsets.US_ASCII
             )
        );
    }

    @Server
    @Override
    public void inbound(RequestRouter router, HandshakeHandler handler) {
        LOGGER.info("Client Hello!");
        LOGGER.info("Client using major protocol " + this.majorProtocol.name() + " version " + this.majorProtocol.version() + " by client: " + this.clientVersion);
        LOGGER.info("Client expected pre shared cipher key: " + this.expectCipherKey);
        if (! this.majorProtocol.forceUse() && this.majorProtocol.compatible() > KalmiaConstant.STANDARD_REQUEST_PROTOCOL.version()) {
            LOGGER.warn("The client protocol is not compatible to server, unable to handshake");

            router.send(new DisconnectPacket("Not compatible protocol version"));

            router.disconnect();
            return;
        }

        String usingCipherKey = KalmiaPreSharedKey.prikeyManager.has(this.expectCipherKey) ? this.expectCipherKey : KalmiaPreSharedKey.defaultCipherKey;
        router.setCrypto(new EcCrypto(null,
                                      KalmiaPreSharedKey.prikeyManager.get(usingCipherKey)
        ));
        router.send(new HandshakePreSharedEcPacket(usingCipherKey));
    }

    @Override
    public byte[] payload() {
        return BytesUtil.concat(this.majorProtocol.toBytes(),
                                new byte[]{(byte) this.clientVersion.length()},
                                this.clientVersion.getBytes(StandardCharsets.UTF_8),
                                new byte[]{(byte) this.expectCipherKey.length()},
                                this.expectCipherKey.getBytes(StandardCharsets.US_ASCII)
        );
    }
}
