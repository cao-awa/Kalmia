package com.github.cao.awa.kalmia.translation.network.packet;

import com.alibaba.fastjson2.JSONObject;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public abstract class TranslationPacket {
    public JSONObject toJSON() {
        return null;
    }

    public TextWebSocketFrame toFrame() {
        return new TextWebSocketFrame(toJSON().toString());
    }
}
