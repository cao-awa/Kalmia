package com.github.cao.awa.kalmia.framework.serialize.json.type.math;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

import java.math.BigDecimal;

@AutoJsonSerializer(target = BigDecimal.class)
public class JsonBigDecimalSerializer implements JsonSerializer<BigDecimal> {
    @Override
    public void serialize(JSONObject json, String key, BigDecimal target) {
        json.put(key,
                 target
        );
    }

    @Override
    public BigDecimal deserialize(JSONObject json, String key) {
        return json.getBigDecimal(key);
    }
}