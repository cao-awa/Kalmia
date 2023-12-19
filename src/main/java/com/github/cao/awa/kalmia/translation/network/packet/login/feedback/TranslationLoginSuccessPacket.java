package com.github.cao.awa.kalmia.translation.network.packet.login.feedback;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.Useless;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotations.translation.Translation;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;

@Translation(type = "login", name = "login_success")
public class TranslationLoginSuccessPacket extends TranslationPacket {
    @AutoData(key = "access_identity")
    @DoNotSet
    private LongAndExtraIdentity accessIdentity;
    @AutoData(key = "token")
    @DoNotSet
    private String token;

    @Auto
    @Useless
    public TranslationLoginSuccessPacket(JSONObject json) {

    }

    public TranslationLoginSuccessPacket(LongAndExtraIdentity accessIdentity, String token) {
        this.accessIdentity = accessIdentity;
        this.token = token;
    }

    @Getter
    public LongAndExtraIdentity accessIdentity() {
        return this.accessIdentity;
    }

    @Getter
    public String token() {
        return this.token;
    }
}
