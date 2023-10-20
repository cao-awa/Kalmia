package com.github.cao.awa.kalmia.framework.serialize.json.type.primitive;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

@AutoJsonSerializer(target = {Long.class, long.class})
public class JsonLongSerializer implements JsonSerializer<Long> {
    @Override
    public void serialize(JSONObject json, String key, Long target) {
        json.put(key,
                 target
        );
    }

    @Override
    public Long deserialize(JSONObject json, String key) {
        return json.getLong(key);
    }
}
