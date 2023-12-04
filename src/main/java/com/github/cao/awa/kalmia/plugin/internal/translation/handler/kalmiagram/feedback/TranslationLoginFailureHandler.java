package com.github.cao.awa.kalmia.plugin.internal.translation.handler.kalmiagram.feedback;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginFailureEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginFailurePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.translation.network.packet.login.feedback.TranslationLoginFailurePacket;

@Auto
@PluginRegister(name = "kalmia_translation")
public class TranslationLoginFailureHandler implements LoginFailureEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, LoginFailurePacket packet) {
        KalmiaTranslationEnv.translationRouter(router)
                            .send(new TranslationLoginFailurePacket(packet.uid(),
                                                                    packet.reason()
                            ));
    }
}
