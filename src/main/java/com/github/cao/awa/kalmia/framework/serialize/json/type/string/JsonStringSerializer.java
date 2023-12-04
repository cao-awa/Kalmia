package com.github.cao.awa.kalmia.framework.serialize.json.type.string;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

@AutoJsonSerializer(target = String.class)
public class JsonStringSerializer implements JsonSerializer<String> {
    @Override
    public void serialize(JSONObject json, String key, String target) {
        json.put(key,
                 target
        );
    }

    @Override
    public String deserialize(JSONObject json, String key) {
        return json.getString(key);
    }
}
