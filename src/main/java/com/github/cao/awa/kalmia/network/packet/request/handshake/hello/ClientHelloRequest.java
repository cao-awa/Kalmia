package com.github.cao.awa.kalmia.network.packet.request.handshake.hello;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.ClientHelloPacket;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

/**
 * @see ClientHelloPacket
 */
@Client
public class ClientHelloRequest extends WritablePacket {
    private static final boolean SHOULD_RSA = true;
    private static final boolean SHOULD_SYM = true;
    private static final long SYM_ID = - 1;
    public static final byte[] ID = SkippedBase256.longToBuf(0);
    private final RequestProtocol protocol;
    private final String clientVersion;

    public ClientHelloRequest(RequestProtocol protocol, String clientVersion) {
        this.protocol = protocol;
        this.clientVersion = clientVersion;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(this.protocol.toBytes(),
                                new byte[]{(byte) this.clientVersion.length()},
                                this.clientVersion.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
