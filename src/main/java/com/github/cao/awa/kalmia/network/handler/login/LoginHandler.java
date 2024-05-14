package com.github.cao.awa.kalmia.network.handler.login;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;

import java.util.Set;

public class LoginHandler extends PacketHandler<LoginHandler> {
    private static final Set<RequestState> ALLOW_STATUS = Manipulate.operation(ApricotCollectionFactor.hashSet(),
                                                                                       set -> {
                                                                                           set.add(RequestState.AUTH);
                                                                                       }
    );

    @Override
    public void inbound(Packet<LoginHandler> packet, RequestRouter router) {
        packet.inbound(router,
                       this
        );
    }

    @Override
    public Set<RequestState> allowStates() {
        return ALLOW_STATUS;
    }
}
