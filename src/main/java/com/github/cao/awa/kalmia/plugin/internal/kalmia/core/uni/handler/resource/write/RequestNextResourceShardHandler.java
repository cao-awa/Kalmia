package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.resource.write;

import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.resource.write.RequestNextResourceShardEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.RequestNextResourceShardPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class RequestNextResourceShardHandler implements RequestNextResourceShardEventHandler {
    @Override
    public void handle(RequestRouter router, RequestNextResourceShardPacket packet) {
        KalmiaEnv.awaitManager.notice(packet.receipt());
    }
}
