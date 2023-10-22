package com.github.cao.awa.kalmia.translation.network.packet;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.annotation.translation.Translation;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.AnnotationUtil;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public abstract class TranslationPacket {
    @Auto
    @Client
    private long clientTimestamp;
    @Auto
    @Client
    private String clientIdentity;

    public TranslationPacket(JSONObject json) {
        try {
            KalmiaEnv.jsonSerializeFramework.create(this,
                                                    json
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TranslationPacket() {

    }

    public long clientTimestamp() {
        return this.clientTimestamp;
    }

    public TranslationPacket clientTimestamp(long timestamp) {
        this.clientTimestamp = timestamp;
        return this;
    }

    public String clientIdentity() {
        return this.clientIdentity;
    }

    public TranslationPacket clientIdentity(String identity) {
        this.clientIdentity = identity;
        return this;
    }

    public JSONObject toJSON(TranslationRouter router) {
        try {
            JSONObject json = new JSONObject();
            JSONObject data = KalmiaEnv.jsonSerializeFramework.payload(this);

            Translation translation = AnnotationUtil.getAnnotation(this,
                                                                   Translation.class
            );

            json.put("post_type",
                     translation.type()
            );
            json.put("post_name",
                     translation.name()
            );
            json.put("time",
                     TimeUtil.millions()
            );
            json.put("identity",
                     router.clientIdentity()
            );
            json.put("data",
                     data
            );

            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TextWebSocketFrame toFrame(TranslationRouter router) {
        return new TextWebSocketFrame(toJSON(router).toString());
    }
}
