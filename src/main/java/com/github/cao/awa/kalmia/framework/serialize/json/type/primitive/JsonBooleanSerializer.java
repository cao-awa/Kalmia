package com.github.cao.awa.kalmia.framework.serialize.json.type.primitive;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

@AutoJsonSerializer(target = {Boolean.class, boolean.class})
public class JsonBooleanSerializer implements JsonSerializer<Boolean> {
    @Override
    public void serialize(JSONObject json, String key, Boolean target) {
        json.put(key,
                 target
        );
    }

    @Override
    public Boolean deserialize(JSONObject json, String key) {
        return json.getBoolean(key);
    }
}
