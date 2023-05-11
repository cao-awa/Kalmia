package com.github.cao.awa.kalmia.network.handler.ping;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public class PingHandler extends PacketHandler<PingHandler> {
    private static final Set<RequestStatus> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(),
                                                                                        set -> {
                                                                                            set.addAll(RequestStatus.all());
                                                                                        }
    );

    @Override
    public void inbound(Packet<PingHandler> packet, RequestRouter router) {
        packet.inbound(router,
                       this
        );
    }

    @Override
    public Set<RequestStatus> allowStatus() {
        return ALLOW_STATUS;
    }
}
