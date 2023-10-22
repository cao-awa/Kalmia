package com.github.cao.awa.kalmia.plugin.internal.translation.handler.kalmiagram.handshake.crypto.ec.pubkey;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.translation.network.packet.meta.status.TranslationProxyStatusPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@PluginRegister(name = "kalmia_translation")
public class HandshakePreSharedEcHandler implements HandshakePreSharedEcEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("PreSharedRsaHandler");

    @Auto
    @Override
    public void handle(RequestRouter router, HandshakePreSharedEcPacket packet) {
        KalmiaTranslationEnv.translationRouter(router)
                            .send(new TranslationProxyStatusPacket("status.kalmia.handshake.ec"));
    }
}
