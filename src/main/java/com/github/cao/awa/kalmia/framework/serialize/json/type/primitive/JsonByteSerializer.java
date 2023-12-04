package com.github.cao.awa.kalmia.framework.serialize.json.type.primitive;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

@AutoJsonSerializer(target = {Byte.class, byte.class})
public class JsonByteSerializer implements JsonSerializer<Byte> {
    @Override
    public void serialize(JSONObject json, String key, Byte target) {
        json.put(key,
                 target
        );
    }

    @Override
    public Byte deserialize(JSONObject json, String key) {
        return json.getByte(key);
    }
}
