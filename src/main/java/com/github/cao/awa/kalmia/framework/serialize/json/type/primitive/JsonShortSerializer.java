package com.github.cao.awa.kalmia.framework.serialize.json.type.primitive;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

@AutoJsonSerializer(target = {Short.class, short.class})
public class JsonShortSerializer implements JsonSerializer<Short> {
    @Override
    public void serialize(JSONObject json, String key, Short target) {
        json.put(key,
                 target
        );
    }

    @Override
    public Short deserialize(JSONObject json, String key) {
        return json.getShort(key);
    }
}
