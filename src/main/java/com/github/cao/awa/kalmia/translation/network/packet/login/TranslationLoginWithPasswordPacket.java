package com.github.cao.awa.kalmia.translation.network.packet.login;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.translation.TranslationEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotation.translation.Translation;
import com.github.cao.awa.kalmia.translation.event.inbound.login.password.TranslationLoginWithPasswordEvent;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Translation(type = "login", name = "login_with_password")
@TranslationEventTarget(TranslationLoginWithPasswordEvent.class)
public class TranslationLoginWithPasswordPacket extends TranslationPacket {
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
    public long uid() {
        return this.uid;
    }

    @Getter
    public String password() {
        return this.password;
    }
}
