package com.github.cao.awa.kalmia.translation.network.packet;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotation.translation.Translation;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.AnnotationUtil;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public abstract class TranslationPacket {
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

    public JSONObject toJSON() {
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
            json.put("data",
                     data
            );

            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TextWebSocketFrame toFrame() {
        return new TextWebSocketFrame(toJSON().toString());
    }
}
