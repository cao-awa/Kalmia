package com.github.cao.awa.kalmia.translation.network.packet.message.notice;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.Useless;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotations.translation.Translation;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.message.display.ClientMessage;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.modmdo.annotation.platform.External;

@External
@Translation(type = "message", name = "new_message_notice")
public class TranslationNewMessageNoticePacket extends TranslationPacket {
    @AutoData(key = "sid")
    @DoNotSet
    private LongAndExtraIdentity sid;
    @AutoData(key = "message")
    @DoNotSet
    private ClientMessage message;

    @Auto
    @Useless
    public TranslationNewMessageNoticePacket(JSONObject json) {

    }

    public TranslationNewMessageNoticePacket(LongAndExtraIdentity sid, ClientMessage message) {
        this.sid = sid;
        this.message = message;
    }

    @Getter
    public LongAndExtraIdentity sid() {
        return this.sid;
    }

    @Getter
    public ClientMessage message() {
        return this.message;
    }
}
