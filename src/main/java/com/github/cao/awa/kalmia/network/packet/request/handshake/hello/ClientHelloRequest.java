package com.github.cao.awa.kalmia.network.packet.request.handshake.hello;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.ClientHelloPacket;
import com.github.cao.awa.kalmia.protocol.RequestProtocolName;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

/**
 * @see ClientHelloPacket
 */
@Client
public class ClientHelloRequest extends Request {
    private static final boolean SHOULD_RSA = true;
    private static final boolean SHOULD_SYM = true;
    private static final long SYM_ID = - 1;
    public static final byte[] ID = SkippedBase256.longToBuf(0);
    private final RequestProtocolName majorProtocol;
    private final String clientVersion;

    public ClientHelloRequest(RequestProtocolName majorProtocol, String clientVersion) {
        this.majorProtocol = majorProtocol;
        this.clientVersion = clientVersion;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(this.majorProtocol.toBytes(),
                                new byte[]{(byte) this.clientVersion.length()},
                                this.clientVersion.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
