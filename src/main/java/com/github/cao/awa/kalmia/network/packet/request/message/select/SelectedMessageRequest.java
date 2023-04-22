package com.github.cao.awa.kalmia.network.packet.request.message.select;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @see SelectedMessagePacket
 */
@Server
public class SelectedMessageRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(13);
    private final long sessionId;
    private final long from;
    private final long to;
    private final List<Message> messages;

    public SelectedMessageRequest(long sessionId, long from, long to, List<Message> messages) {
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
        this.messages = messages;
    }

    @Override
    public byte[] data() {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            for (Message msg : this.messages) {
                output.write(msg.toBytes());
            }
            return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                    SkippedBase256.longToBuf(this.from),
                                    SkippedBase256.longToBuf(this.to),
                                    output.toByteArray()
            );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
