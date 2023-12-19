package com.github.cao.awa.kalmia.plugin.internal.translation.handler.login;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.translation.event.handler.inbound.login.password.TranslationLoginWithPasswordEventHandler;
import com.github.cao.awa.kalmia.translation.network.packet.login.TranslationLoginWithPasswordPacket;
import com.github.cao.awa.kalmia.translation.network.packet.meta.status.TranslationProxyStatusPacket;

@Auto
@PluginRegister(name = "kalmia_translation")
public class TranslationLoginWithPasswordHandler implements TranslationLoginWithPasswordEventHandler {
    @Auto
    @Override
    public void handle(TranslationRouter router, TranslationLoginWithPasswordPacket packet) {
        RequestRouter requestRouter = KalmiaTranslationEnv.router(router);

        System.out.println(packet.accessIdentity() + ":" + packet.password());

        router.send(new TranslationProxyStatusPacket("status.kalmia.login"));

        requestRouter.send(new LoginWithPasswordPacket(packet.accessIdentity(),
                                                       packet.password()
        ));
    }
}
