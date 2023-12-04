package com.github.cao.awa.kalmia.translation.network.packet.meta.status;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.Useless;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotations.translation.Translation;
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
