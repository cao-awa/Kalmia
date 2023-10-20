package com.github.cao.awa.kalmia.translation.network.packet.meta.connect;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.translation.TranslationEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotation.translation.Translation;
import com.github.cao.awa.kalmia.event.translation.inbound.TranslationProxyConnectEvent;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;

@Translation(type = "meta", name = "status_notice")
@TranslationEventTarget(TranslationProxyConnectEvent.class)
public class TranslationProxyStatusPacket extends TranslationPacket {
    @AutoData(key = "status")
    @DoNotSet
    private String status;

    @Auto
    public TranslationProxyStatusPacket(JSONObject json) {
        super(json);
    }

    public TranslationProxyStatusPacket(String status) {
        this.status = status;
    }

    @Getter
    public String status() {
        return this.status;
    }
}
