package com.github.cao.awa.kalmia.translation.network.packet.meta.connect;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.translation.TranslationEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.annotations.translation.Translation;
import com.github.cao.awa.kalmia.translation.event.inbound.meta.connect.TranslationProxyConnectEvent;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Translation(type = "meta", name = "proxy_connect")
@TranslationEventTarget(TranslationProxyConnectEvent.class)
public class TranslationProxyConnectPacket extends TranslationPacket {
    @AutoData(key = "cipher")
    @DoNotSet
    private String cipher;
    @AutoData(key = "identity")
    @DoNotSet
    private String identity;
    @AutoData(key = "server_host")
    @DoNotSet
    private String host;
    @AutoData(key = "server_port")
    @DoNotSet
    private int port;
    @AutoData(key = "data_save")
    @DoNotSet
    private boolean dataSave;

    @Auto
    @Server
    public TranslationProxyConnectPacket(JSONObject json) {
        super(json);
    }

    @Getter
    public String cipher() {
        return this.cipher;
    }

    @Getter
    public String clientIdentity() {
        return this.identity;
    }

    @Getter
    public String host() {
        return this.host;
    }

    @Getter
    public int port() {
        return this.port;
    }

    @Getter
    public boolean dataSave() {
        return this.dataSave;
    }
}
