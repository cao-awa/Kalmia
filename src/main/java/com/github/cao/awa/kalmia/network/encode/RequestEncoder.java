package com.github.cao.awa.kalmia.network.encode;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.count.TrafficCount;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestEncoder extends MessageToByteEncoder<Request> {
    private final RequestRouter router;

    public RequestEncoder(RequestRouter router) {
        this.router = router;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) throws Exception {
        // Commit traffic count.
        TrafficCount.encode(msg.data().length + msg.id().length);

        // Encode it by router.
        byte[] data = msg.encode(this.router);

        // Mark the length for frame reading.
        byte[] length = SkippedBase256.intToBuf(data.length);

        // Write packet length and data
        out.writeBytes(length);
        out.writeBytes(data);

        // Commit traffic count.
        TrafficCount.send(data.length + length.length);
    }
}
