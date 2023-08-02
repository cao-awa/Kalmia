package com.github.cao.awa.kalmia.network.handler.stateless;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public class StatelessHandler extends PacketHandler<StatelessHandler> {
    private static final Set<RequestState> ALLOW_STATES = EntrustEnvironment.operation(ApricotCollectionFactor.hashSet(),
                                                                                       set -> {
                                                                                           set.addAll(RequestState.all());
                                                                                       }
    );

    @Override
    public void inbound(Packet<StatelessHandler> packet, RequestRouter router) {
        packet.inbound(router,
                       this
        );
    }

    @Override
    public Set<RequestState> allowStates() {
        return ALLOW_STATES;
    }
}
