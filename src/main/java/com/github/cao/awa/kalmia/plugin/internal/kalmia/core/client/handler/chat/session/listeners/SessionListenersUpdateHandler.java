package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.chat.session.listeners;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.client.polling.PollingClient;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.in.SessionListenersUpdateEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners.SessionListenersUpdatePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class SessionListenersUpdateHandler implements SessionListenersUpdateEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("SessionListenersUpdateHandler");

    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SessionListenersUpdatePacket packet) {
        Kalmia.CLIENT.userManager()
                     .sessionListeners(router.accessIdentity(),
                                       packet.mapId()
                     );

        packet.sessions()
              .forEach(session -> {
                  Kalmia.CLIENT.sessionManager()
                               .set(session.identity(),
                                    session
                               );
              });

        PollingClient.CLIENT.sessionListenersIdentity(BytesRandomIdentifier.create(24));
    }
}
