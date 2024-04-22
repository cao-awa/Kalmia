package com.github.cao.awa.kalmia.translation.network.packet.login;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.translation.TranslationEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.serializer.Missing;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotations.translation.Translation;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.translation.event.inbound.login.password.TranslationLoginWithPasswordEvent;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Server
@Translation(type = "login", name = "login_with_password")
@TranslationEventTarget(TranslationLoginWithPasswordEvent.class)
public class TranslationLoginWithPasswordPacket extends TranslationPacket {
    @AutoData(key = "access_identity")
    @DoNotSet
    @Missing(standby = "uid")
    private LongAndExtraIdentity accessIdentity;
    @AutoData(key = "uid")
    @DoNotSet
    private long uid;
    @AutoData(key = "pwd")
    @DoNotSet
    private String password;

    @Auto
    @Server
    public TranslationLoginWithPasswordPacket(JSONObject json) {
        super(json);
    }

    @Getter
    public LongAndExtraIdentity accessIdentity() {
        return this.accessIdentity;
    }

    @Getter
    public long uid() {
        return this.uid;
    }

    @Getter
    public String password() {
        return this.password;
    }
}
