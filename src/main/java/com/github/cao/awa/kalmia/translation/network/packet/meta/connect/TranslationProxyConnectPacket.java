package com.github.cao.awa.kalmia.translation.network.packet.meta.connect;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.translation.TranslationEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotation.translation.Translation;
import com.github.cao.awa.kalmia.event.translation.inbound.TranslationProxyConnectEvent;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;

@Translation(type = "meta", name = "proxy_connect")
@TranslationEventTarget(TranslationProxyConnectEvent.class)
public class TranslationProxyConnectPacket extends TranslationPacket {
    @AutoData(key = "cipher")
    @DoNotSet
    private String cipher;

    public TranslationProxyConnectPacket(JSONObject json) {
        super(json);
    }

    @Getter
    public String cipher() {
        return this.cipher;
    }
}
