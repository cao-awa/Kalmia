package com.github.cao.awa.kalmia.network.packet.request.message.send;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see SendMessagePacket
 */
@Server
public class SentMessageRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(11);
    private final long seq;
    private final byte[] identity;

    public SentMessageRequest(long seq, byte[] identity) {
        this.seq = seq;
        this.identity = identity;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.seq),
                                this.identity
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
