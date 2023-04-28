package com.github.cao.awa.kalmia.network.packet.request.message.delete;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeleteMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see DeleteMessagePacket
 */
@Client
public class DeleteMessageRequest extends Request {
    public static final byte[] ID = SkippedBase256.longToBuf(14);
    private final long sessionId;
    private final long seq;

    public DeleteMessageRequest(long sessionId, long seq) {
        this.sessionId = sessionId;
        this.seq = seq;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                SkippedBase256.longToBuf(this.seq)
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
