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
import com.github.cao.awa.modmdo.annotation.platform.External;

@External
@Translation(type = "login", name = "login_failure")
public class TranslationLoginFailurePacket extends TranslationPacket {
    @AutoData(key = "access_identity")
    @DoNotSet
    private LongAndExtraIdentity accessIdentity;
    @AutoData(key = "reason")
    @DoNotSet
    private String reason;

    @Auto
    @Useless
    public TranslationLoginFailurePacket(JSONObject json) {

    }

    public TranslationLoginFailurePacket(LongAndExtraIdentity accessIdentity, String reason) {
        this.accessIdentity = accessIdentity;
        this.reason = reason;
    }

    @Getter
    public LongAndExtraIdentity accessIdentity() {
        return this.accessIdentity;
    }

    @Getter
    public String reason() {
        return this.reason;
    }
}
