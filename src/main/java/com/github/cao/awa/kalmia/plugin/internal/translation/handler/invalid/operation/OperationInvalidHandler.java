package com.github.cao.awa.kalmia.plugin.internal.translation.handler.invalid.operation;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.invalid.operation.OperationInvalidEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@PluginRegister(name = "kalmia_translation")
public class OperationInvalidHandler implements OperationInvalidEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("OperationInvalidHandler");

    @Override
    public void handle(RequestRouter router, OperationInvalidPacket packet) {
        LOGGER.info("Operation invalid because: {}",
                    packet.reason()
        );
    }
}
