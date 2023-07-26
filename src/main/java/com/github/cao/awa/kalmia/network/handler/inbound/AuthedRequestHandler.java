package com.github.cao.awa.kalmia.network.handler.inbound;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public class AuthedRequestHandler extends PacketHandler<AuthedRequestHandler> {
    private static final Set<RequestState> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.hashSet(),
                                                                                       set -> {
                                                                                           set.add(RequestState.AUTHED);
                                                                                       }
    );

    private long uid;

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public void inbound(Packet<AuthedRequestHandler> packet, RequestRouter router) {
        packet.inbound(router,
                       this
        );
    }

    @Override
    public Set<RequestState> allowStates() {
        return ALLOW_STATUS;
    }
}
