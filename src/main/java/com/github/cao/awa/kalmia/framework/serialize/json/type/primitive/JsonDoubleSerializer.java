package com.github.cao.awa.kalmia.framework.serialize.json.type.primitive;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

@AutoJsonSerializer(target = {Double.class, double.class})
public class JsonDoubleSerializer implements JsonSerializer<Double> {
    @Override
    public void serialize(JSONObject json, String key, Double target) {
        json.put(key,
                 target
        );
    }

    @Override
    public Double deserialize(JSONObject json, String key) {
        return json.getDouble(key);
    }
}
