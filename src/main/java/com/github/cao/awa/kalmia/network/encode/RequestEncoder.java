package com.github.cao.awa.kalmia.network.encode;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Arrays;

public class RequestEncoder extends MessageToByteEncoder<WritablePacket> {
    private final UnsolvedRequestRouter router;

    public RequestEncoder(UnsolvedRequestRouter router) {
        this.router = router;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, WritablePacket msg, ByteBuf out) throws Exception {
        byte[] data = msg.encode(this.router);;
        out.writeInt(data.length);
        out.writeBytes(data);

        System.out.println("Write: " + Arrays.toString(data));

        out.markWriterIndex();
    }
}
