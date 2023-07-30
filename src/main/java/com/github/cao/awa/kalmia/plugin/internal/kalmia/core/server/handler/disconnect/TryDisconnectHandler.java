package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.disconnect;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.disconnect.TryDisconnectEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.TryDisconnectPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class TryDisconnectHandler implements TryDisconnectEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("TryDisconnectHandler");

    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, TryDisconnectPacket packet) {
        LOGGER.info("Disconnecting from request");
        router.disconnect();
    }
}
