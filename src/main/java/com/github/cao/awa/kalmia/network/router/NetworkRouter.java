package com.github.cao.awa.kalmia.network.router;

import com.github.cao.awa.kalmia.KalmiaServer;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkRouter extends SimpleChannelInboundHandler<UnsolvedPacket> {

}
