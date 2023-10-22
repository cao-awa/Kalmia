package com.github.cao.awa.kalmia.translation.network.packet.meta.status;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotation.Useless;
import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotation.translation.Translation;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Translation(type = "meta", name = "status_notice")
public class TranslationProxyStatusPacket extends TranslationPacket {
    @AutoData(key = "status")
    @DoNotSet
    private String status;

    @Auto
    @Useless
    public TranslationProxyStatusPacket(JSONObject json) {
        super(json);
    }

    @Client
    public TranslationProxyStatusPacket(String status) {
        this.status = status;
    }

    @Getter
    public String status() {
        return this.status;
    }
}
