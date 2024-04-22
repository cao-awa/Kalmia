package com.github.cao.awa.kalmia.translation.network.packet.message.select;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.Useless;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotations.translation.Translation;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.message.display.ClientMessage;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.modmdo.annotation.platform.External;

import java.util.List;

@External
@Translation(type = "message", name = "selected_message")
public class TranslationSelectedMessagePacket extends TranslationPacket {
    @AutoData(key = "sid")
    @DoNotSet
    private PureExtraIdentity sid;
    @AutoData(key = "messages")
    @DoNotSet
    private List<ClientMessage> messages;

    @Auto
    @Useless
    public TranslationSelectedMessagePacket(JSONObject json) {

    }

    public TranslationSelectedMessagePacket(PureExtraIdentity sid, List<ClientMessage> messages) {
        this.sid = sid;
        this.messages = messages;
    }

    @Getter
    public PureExtraIdentity sid() {
        return this.sid;
    }

    @Getter
    public List<ClientMessage> messages() {
        return this.messages;
    }
}
