package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.env.KalmiaPreSharedKey;
import com.github.cao.awa.kalmia.network.encode.crypto.asymmetric.rsa.RsaCrypto;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakePreSharedRsaRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ClientHelloRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.protocol.RequestProtocolName;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.nio.charset.StandardCharsets;

/**
 * @see ClientHelloRequest
 */
@Server
@AutoSolvedPacket(0)
public class ClientHelloPacket extends ReadonlyPacket<HandshakeHandler> {
    private final RequestProtocolName majorProtocol;
    private final String clientVersion;
    private final String expectCipherKey;

    public ClientHelloPacket(BytesReader reader) {
        this.majorProtocol = RequestProtocolName.create(reader);
        this.clientVersion = new String(reader.read(reader.read()),
                                        StandardCharsets.UTF_8
        );
        this.expectCipherKey = new String(reader.read(reader.read()),
                                          StandardCharsets.US_ASCII
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
        router.setCrypto(new RsaCrypto(null,
                                       KalmiaPreSharedKey.prikeyManager.get(usingCipherKey)
        ));
        router.send(new HandshakePreSharedRsaRequest(usingCipherKey));
    }
}
