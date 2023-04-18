package com.github.cao.awa.kalmia.network.encode;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.count.TrafficCount;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RequestDecoder extends ByteToMessageDecoder {
    private final UnsolvedRequestRouter router;

    public RequestDecoder(UnsolvedRequestRouter router) {
        this.router = router;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() > 1) {
            byte skipped = in.readByte();

            byte[] length = new byte[skipped == - 1 ? 4 : skipped];
            in.readBytes(length);
            byte[] data = new byte[SkippedBase256.readInt(new BytesReader(BytesUtil.concat(new byte[]{skipped},
                                                                                           length
            )))];
            in.readBytes(data);

            TrafficCount.receive(4 + data.length);

            data = this.router.decode(data);

            TrafficCount.decode(data.length);

            BytesReader reader = new BytesReader(data);

            long id = SkippedBase256.readLong(reader);

            byte[] receipt = reader.read() == - 1 ? reader.non() : reader.read(16);

            out.add(UnsolvedPacketFactor.create(id,
                                                reader.all(),
                                                receipt
            ));
        }
    }
}