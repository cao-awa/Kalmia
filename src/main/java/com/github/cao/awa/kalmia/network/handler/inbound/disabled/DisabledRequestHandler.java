package com.github.cao.awa.kalmia.network.handler.inbound.disabled;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.invalid.operation.OperationInvalidRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public class DisabledRequestHandler extends AuthedRequestHandler {
    private static final Set<RequestStatus> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(),
                                                                                        set -> {
                                                                                            set.add(RequestStatus.AUTHED);
                                                                                            set.add(RequestStatus.DISABLED);
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
    public void inbound(ReadonlyPacket<AuthedRequestHandler> packet, RequestRouter router) {
//        packet.inbound(router,
//                       this
//        );
        router.send(new OperationInvalidRequest("This account has been disabled"));
    }

    @Override
    public Set<RequestStatus> allowStatus() {
        return ALLOW_STATUS;
    }
}
