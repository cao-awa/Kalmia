package com.github.cao.awa.kalmia.translation.network.packet.message.select;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.translation.TranslationEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotations.translation.Translation;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.translation.event.inbound.message.select.TranslationSelectMessageEvent;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Server
@Translation(type = "message", name = "select_message")
@TranslationEventTarget(TranslationSelectMessageEvent.class)
public class TranslationSelectMessagePacket extends TranslationPacket {
    @AutoData(key = "session_identity")
    @DoNotSet
    private PureExtraIdentity sessionIdentity;
    @AutoData(key = "from")
    @DoNotSet
    private long from;
    @AutoData(key = "to")
    @DoNotSet
    private long to;

    @Auto
    @Server
    public TranslationSelectMessagePacket(JSONObject json) {
        super(json);
    }

    @Getter
    public PureExtraIdentity sessionIdentity() {
        return this.sessionIdentity;
    }

    @Getter
    public long from() {
        return this.from;
    }

    @Getter
    public long to() {
        return this.to;
    }
}
