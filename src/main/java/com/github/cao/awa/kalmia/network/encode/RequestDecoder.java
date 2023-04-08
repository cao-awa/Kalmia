package com.github.cao.awa.kalmia.network.encode;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

public class RequestDecoder extends ByteToMessageDecoder {
    private final UnsolvedRequestRouter router;

    public RequestDecoder(UnsolvedRequestRouter router) {
        this.router = router;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() > 4) {
            byte[] data = new byte[in.readInt()];
            in.readBytes(data);

            in.markReaderIndex();

            data = this.router.decode(data);

            System.out.println("Read: " + Arrays.toString(data));

            BytesReader reader = new BytesReader(data);

            long id = SkippedBase256.readLong(reader);

            out.add(UnsolvedPacketFactor.create(id,
                                                reader.all()
            ));
        }
    }
}