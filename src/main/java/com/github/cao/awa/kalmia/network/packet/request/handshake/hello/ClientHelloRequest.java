package com.github.cao.awa.kalmia.network.packet.request.handshake.hello;

import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.rsa.pubkey.HandshakePreSharedRsaPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.ClientHelloPacket;
import com.github.cao.awa.kalmia.protocol.RequestProtocolName;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

/**
 * @see ClientHelloPacket
 * @see HandshakePreSharedRsaPacket
 */
@Client
public class ClientHelloRequest extends Request {
    private static final boolean SHOULD_RSA = true;
    private static final boolean SHOULD_SYM = true;
    private static final long SYM_ID = - 1;
    public static final byte[] ID = SkippedBase256.longToBuf(0);
    private final RequestProtocolName majorProtocol;
    private final String clientVersion;
    private final String expectCipherKey;

    public ClientHelloRequest(RequestProtocolName majorProtocol, String clientVersion, String expectCipherKey) {
        this.majorProtocol = majorProtocol;
        this.clientVersion = clientVersion;
        this.expectCipherKey = expectCipherKey;
    }

    public ClientHelloRequest(RequestProtocolName majorProtocol, String clientVersion) {
        this.majorProtocol = majorProtocol;
        this.clientVersion = clientVersion;
        this.expectCipherKey = KalmiaEnv.expectCipherKey;
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

    @Override
    public byte[] id() {
        return ID;
    }
}
