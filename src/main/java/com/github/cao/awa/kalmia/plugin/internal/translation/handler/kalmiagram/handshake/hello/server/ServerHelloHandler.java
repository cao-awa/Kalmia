package com.github.cao.awa.kalmia.plugin.internal.translation.handler.kalmiagram.handshake.hello.server;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.hello.server.ServerHelloEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server.ServerHelloPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@PluginRegister(name = "kalmia_translation")
public class ServerHelloHandler implements ServerHelloEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("ServerHelloHandler");

    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, ServerHelloPacket packet) {
        // TODO
        // status.kalmia.handshake.hello
    }
}