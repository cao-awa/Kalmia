package com.github.cao.awa.kalmia.network.handler.handshake;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.dual.DualPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public class HandshakeHandler extends PacketHandler<HandshakeHandler> {
    private static final Set<RequestStatus> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(),
                                                                                        set -> {
                                                                                            set.add(RequestStatus.HELLO);
                                                                                        }
    );

    public HandshakeHandler() {

    }

    @Override
    public void inbound(DualPacket<HandshakeHandler> packet, RequestRouter router) {
        packet.inbound(router,
                       this
        );
    }

    @Override
    public Set<RequestStatus> allowStatus() {
        return ALLOW_STATUS;
    }
}
