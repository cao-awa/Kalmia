package com.github.cao.awa.kalmia.plugin.internal.translation.handler.meta;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.translation.handler.inbound.TranslationProxyConnectEventHandler;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.translation.network.packet.meta.TranslationProxyConnectPacket;

@Auto
@PluginRegister(name = "kalmia_translation")
public class TranslationProxyConnectHandler implements TranslationProxyConnectEventHandler {
    @Auto
    @Override
    public void handle(TranslationRouter router, TranslationProxyConnectPacket packet) {
        System.out.println(packet.cipher());
    }
}
