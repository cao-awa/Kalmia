package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.env.KalmiaPreSharedKey;
import com.github.cao.awa.kalmia.network.encode.crypto.asymmetric.ac.EcCrypto;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.protocol.RequestProtocolName;
import com.github.cao.awa.modmdo.annotation.platform.Generic;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

@Generic
@AutoSolvedPacket(0)
public class ClientHelloPacket extends Packet<HandshakeHandler> {
    private static final boolean SHOULD_RSA = true;
    private static final boolean SHOULD_SYM = true;
    private static final long SYM_ID = - 1;
    private final RequestProtocolName majorProtocol;
    private final String clientVersion;
    private final String expectCipherKey;

    public ClientHelloPacket(RequestProtocolName majorProtocol, String clientVersion, String expectCipherKey) {
        this.majorProtocol = majorProtocol;
        this.clientVersion = clientVersion;
        this.expectCipherKey = expectCipherKey;
    }

    public ClientHelloPacket(RequestProtocolName majorProtocol, String clientVersion) {
        this(majorProtocol,
             clientVersion,
             KalmiaPreSharedKey.expectCipherKey
        );
    }

    public ClientHelloPacket(BytesReader reader) {
        this(RequestProtocolName.create(reader),
             new String(reader.read(reader.read()),
                        StandardCharsets.UTF_8
             ),
             new String(reader.read(reader.read()),
                        StandardCharsets.US_ASCII
             )
        );
    }

    @Override
    public void inbound(RequestRouter router, HandshakeHandler handler) {
        System.out.println("Client Hello!");
        System.out.println("Client using major protocol " + this.majorProtocol.name() + " version " + this.majorProtocol.version() + " by client: " + this.clientVersion);
        System.out.println("Client expected pre shared cipher key: " + this.expectCipherKey);
        if (this.majorProtocol.version() > KalmiaEnv.STANDARD_REQUEST_PROTOCOL.version()) {
            System.out.println("WARN: the protocol is future version, not compatible!");
        }
        String usingCipherKey = KalmiaPreSharedKey.prikeyManager.has(this.expectCipherKey) ? this.expectCipherKey : KalmiaPreSharedKey.defaultCipherKey;
        router.setCrypto(new EcCrypto(null,
                                      KalmiaPreSharedKey.prikeyManager.get(usingCipherKey)
        ));
        router.send(new HandshakePreSharedEcPacket(usingCipherKey));
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(this.majorProtocol.toBytes(),
                                new byte[]{(byte) this.clientVersion.length()},
                                this.clientVersion.getBytes(StandardCharsets.UTF_8),
                                new byte[]{(byte) this.expectCipherKey.length()},
                                this.expectCipherKey.getBytes(StandardCharsets.US_ASCII)
        );
    }
}
