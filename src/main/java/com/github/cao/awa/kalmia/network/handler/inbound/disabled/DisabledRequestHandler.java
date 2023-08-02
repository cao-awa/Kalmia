package com.github.cao.awa.kalmia.network.handler.inbound.disabled;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public class DisabledRequestHandler extends AuthedRequestHandler {
    private static final Set<RequestState> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.hashSet(),
                                                                                       set -> {
                                                                                           set.add(RequestState.AUTHED);
                                                                                           set.add(RequestState.DISABLED);
                                                                                       }
    );

    private long uid;

    public long uid() {
        return this.uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public void inbound(Packet<AuthedRequestHandler> packet, RequestRouter router) {
//        packet.inbound(router,
//                       this
//        );
        router.send(new OperationInvalidPacket("This account has been disabled"));
    }

    @Override
    public Set<RequestState> allowStates() {
        return ALLOW_STATUS;
    }
}
