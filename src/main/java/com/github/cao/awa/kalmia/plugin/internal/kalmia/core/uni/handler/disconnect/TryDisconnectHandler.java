package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.disconnect;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.event.handler.network.inbound.disconnect.TryDisconnectEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.TryDisconnectPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TryDisconnectHandler implements TryDisconnectEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("TryDisconnectHandler");

    @Auto
    @Override
    public void handle(RequestRouter router, TryDisconnectPacket packet) {
        LOGGER.info("Disconnecting from request, reason: {}",
                    packet.reason()
        );
        router.disconnect();
    }
}
