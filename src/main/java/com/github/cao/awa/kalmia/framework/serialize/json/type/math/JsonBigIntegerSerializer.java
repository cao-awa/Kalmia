package com.github.cao.awa.kalmia.framework.serialize.json.type.math;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

import java.math.BigInteger;

@AutoJsonSerializer(target = BigInteger.class)
public class JsonBigIntegerSerializer implements JsonSerializer<BigInteger> {
    @Override
    public void serialize(JSONObject json, String key, BigInteger target) {
        json.put(key,
                 target
        );
    }

    @Override
    public BigInteger deserialize(JSONObject json, String key) {
        return json.getBigInteger(key);
    }
}