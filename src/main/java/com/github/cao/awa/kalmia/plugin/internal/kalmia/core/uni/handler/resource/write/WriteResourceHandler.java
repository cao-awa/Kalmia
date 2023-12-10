package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.resource.write;

import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.resource.write.WriteResourceEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.WriteResourceNextStepPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.WriteResourcePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class WriteResourceHandler implements WriteResourceEventHandler {
    @Override
    public void handle(RequestRouter router, WriteResourcePacket packet) {
        KalmiaEnv.resourceManager.requestWrite(Mathematics.radix(packet.receipt(),
                                                                 36
                                               ),
                                               packet.data(),
                                               packet.isFinal(),
                                               () -> {
                                                   if (! packet.isFinal()) {
                                                       router.send(new WriteResourceNextStepPacket(false).receipt(packet.receipt()));
                                                   }
                                               }
        );
    }
}
