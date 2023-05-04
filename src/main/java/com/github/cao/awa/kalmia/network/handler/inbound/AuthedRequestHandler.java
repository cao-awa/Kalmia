package com.github.cao.awa.kalmia.network.handler.inbound;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.dual.DualPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public class AuthedRequestHandler extends PacketHandler<AuthedRequestHandler> {
    private static final Set<RequestStatus> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(),
                                                                                        set -> {
                                                                                            set.add(RequestStatus.AUTHED);
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
    public void inbound(DualPacket<AuthedRequestHandler> packet, RequestRouter router) {
        packet.inbound(router,
                       this
        );
    }

    @Override
    public Set<RequestStatus> allowStatus() {
        return ALLOW_STATUS;
    }
}
