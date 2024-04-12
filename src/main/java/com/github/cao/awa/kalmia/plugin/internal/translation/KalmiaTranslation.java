package com.github.cao.awa.kalmia.plugin.internal.translation;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.annotations.auto.AutoPlugin;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.KalmiaEventBus;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import com.github.cao.awa.kalmia.translation.network.packet.login.feedback.TranslationLoginFailurePacket;
import com.github.cao.awa.kalmia.translation.network.packet.login.feedback.TranslationLoginSuccessPacket;
import com.github.cao.awa.kalmia.translation.network.packet.meta.status.TranslationProxyStatusPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@AutoPlugin(name = "kalmia_translation", uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE817")
public class KalmiaTranslation extends Plugin {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaTranslation");

    @Override
    public void onLoad() {
        LOGGER.info("Loading kalmia translation");

        KalmiaEventBus.handshakePreSharedEc.trigger((router, receipt, cipherField) -> {
            KalmiaTranslationEnv.translationRouter(router).send(new TranslationProxyStatusPacket("status.kalmia.handshake.ec"));
        });

        KalmiaEventBus.loginFailure.trigger((router, receipt, uid, reason) -> {
            KalmiaTranslationEnv.translationRouter(router).send(new TranslationLoginFailurePacket(uid, reason));
        });

        KalmiaEventBus.loginSuccess.trigger((router, receipt, uid, token) -> {
            KalmiaTranslationEnv.translationRouter(router).send(new TranslationLoginSuccessPacket(uid, Mathematics.radix(token, 36)));
        });

        KalmiaEventBus.serverHello.trigger((router, receipt, testKey, testSha, iv) -> {
            KalmiaTranslationEnv.translationRouter(router).send(new TranslationProxyStatusPacket("status.kalmia.handshake.hello"));
        });
    }

    @Override
    public boolean canLoad() {
        return KalmiaServer.serverBootstrapConfig.getTranslation().getEnable();
    }
}
