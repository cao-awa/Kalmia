package com.github.cao.awa.kalmia.message.display.serializer;


import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;
import com.github.cao.awa.kalmia.message.display.ClientMessage;

@AutoJsonSerializer(target = {ClientMessage.class})
public class ClientMessageExternalSerializer implements JsonSerializer<ClientMessage> {
    @Override
    public void serialize(JSONObject json, String key, ClientMessage target) {
        json.put(key,
                 target.export()
        );

        System.out.println(target.export());
    }

    @Override
    public ClientMessage deserialize(JSONObject json, String key) {
        return ClientMessage.create(json.getJSONObject(key));
    }
}
