package com.github.cao.awa.kalmia.network.router;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class NetworkRouter extends SimpleChannelInboundHandler<UnsolvedPacket<?>> {

}
