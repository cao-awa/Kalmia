package com.github.cao.awa.kalmia.network.encode;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.count.TrafficCount;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestEncoder extends MessageToByteEncoder<Packet<?>> {
    private static final Logger LOGGER = LogManager.getLogger("RequestEncoder");
    private final RequestRouter router;

    public RequestEncoder(RequestRouter router) {
        this.router = router;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet<?> request, ByteBuf out) throws Exception {
        // Encode it by router.
        byte[] payload = request.encode(this.router);

        // Commit traffic count.
        TrafficCount.encoded(payload.length);

        // Mark the length for frame reading.
        byte[] lengthMark = SkippedBase256.intToBuf(payload.length);

        // Write packet length and payload data
        out.writeBytes(lengthMark);
        out.writeBytes(payload);

        // Commit traffic count.
        TrafficCount.sent(payload.length + lengthMark.length);
    }
}
