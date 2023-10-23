package com.github.cao.awa.kalmia.plugin.internal.translation.handler.kalmiagram.feedback;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginSuccessEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.translation.network.packet.login.feedback.TranslationLoginSuccessPacket;

@Auto
@PluginRegister(name = "kalmia_translation")
public class TranslationLoginSuccessHandler implements LoginSuccessEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, LoginSuccessPacket packet) {
        KalmiaTranslationEnv.translationRouter(router)
                            .send(new TranslationLoginSuccessPacket(packet.uid(),
                                                                    Mathematics.radix(packet.token(),
                                                                                      36
                                                                    )
                            ));
    }
}
