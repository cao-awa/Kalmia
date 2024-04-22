package com.github.cao.awa.kalmia.plugin.internal.translation.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.translation.event.handler.inbound.message.select.TranslationSelectMessageEventHandler;
import com.github.cao.awa.kalmia.translation.network.packet.message.select.TranslationSelectMessagePacket;
import com.github.cao.awa.kalmia.translation.network.router.TranslationRouter;

@Auto
@PluginRegister(name = "kalmia_translation")
public class TranslationSelectMessageHandler implements TranslationSelectMessageEventHandler {
    @Auto
    @Override
    public void handle(TranslationRouter router, TranslationSelectMessagePacket packet) {
        RequestRouter requestRouter = KalmiaTranslationEnv.router(router);

        requestRouter.send(new SelectMessagePacket(
                packet.sessionIdentity(),
                packet.from(),
                packet.to()
        ));
    }
}
