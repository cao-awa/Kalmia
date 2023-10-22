package com.github.cao.awa.kalmia.translation.network.packet.login.feedback;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotation.Useless;
import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotation.translation.Translation;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;

@Translation(type = "login", name = "login_failure")
public class TranslationLoginFailurePacket extends TranslationPacket {
    @AutoData(key = "uid")
    @DoNotSet
    private long uid;
    @AutoData(key = "reason")
    @DoNotSet
    private String reason;

    @Auto
    @Useless
    public TranslationLoginFailurePacket(JSONObject json) {

    }

    public TranslationLoginFailurePacket(long uid, String reason) {
        this.uid = uid;
        this.reason = reason;
    }

    @Getter
    public long uid() {
        return this.uid;
    }

    @Getter
    public String reason() {
        return this.reason;
    }
}
