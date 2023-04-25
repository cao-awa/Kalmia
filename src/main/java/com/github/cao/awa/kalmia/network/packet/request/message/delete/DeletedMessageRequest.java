package com.github.cao.awa.kalmia.network.packet.request.message.delete;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeletedMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see DeletedMessagePacket
 */
@Server
public class DeletedMessageRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(15);
    private final long sid;
    private final long seq;

    public DeletedMessageRequest(long sid, long seq) {
        this.sid = sid;
        this.seq = seq;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sid),
                                SkippedBase256.longToBuf(this.seq)
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
