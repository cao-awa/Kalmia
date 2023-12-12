package com.github.cao.awa.kalmia.event.kalmiagram.handler.network;

import com.github.cao.awa.kalmia.annotations.inaction.DoNotOverride;
import com.github.cao.awa.kalmia.client.polling.PollingClient;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.EventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public interface NetworkEventHandler<P extends Packet<?>, E extends NetworkEvent<P>> extends EventHandler<E> {
    void handle(RequestRouter router, P packet);

    @Override
    @DoNotOverride
    default void handle(E event) {
        RequestRouter router = event.router();
        P packet = event.packet();

        handle(
                router,
                packet
        );

        KalmiaEnv.awaitManager.notice(packet.receipt());

        if (PollingClient.CLIENT != null) {
            PollingClient.CLIENT.stackingNotice(event);
        }
    }
}
