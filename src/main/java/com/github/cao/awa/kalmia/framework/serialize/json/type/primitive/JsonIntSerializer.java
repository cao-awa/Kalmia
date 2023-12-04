package com.github.cao.awa.kalmia.framework.serialize.json.type.primitive;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

@AutoJsonSerializer(target = {Integer.class, int.class})
public class JsonIntSerializer implements JsonSerializer<Integer> {
    @Override
    public void serialize(JSONObject json, String key, Integer target) {
        json.put(key,
                 target
        );
    }

    @Override
    public Integer deserialize(JSONObject json, String key) {
        return json.getInteger(key);
    }
}
