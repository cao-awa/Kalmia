package com.github.cao.awa.kalmia.plugin.internal.translation.handler.login;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.translation.event.handler.inbound.login.password.TranslationLoginWithPasswordEventHandler;
import com.github.cao.awa.kalmia.translation.network.packet.login.TranslationLoginWithPasswordPacket;
import com.github.cao.awa.kalmia.translation.network.packet.login.feedback.TranslationLoginFailurePacket;

@Auto
@PluginRegister(name = "kalmia_translation")
public class TranslationLoginWithPasswordHandler implements TranslationLoginWithPasswordEventHandler {
    @Override
    public void handle(TranslationRouter router, TranslationLoginWithPasswordPacket packet) {
        System.out.println(packet.uid() + ":" + packet.password());

        router.send(new TranslationLoginFailurePacket(packet.uid(),
                                                      "not.done.feature"
        ));
    }
}
