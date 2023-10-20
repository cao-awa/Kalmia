package com.github.cao.awa.kalmia.framework.serialize.json.type.primitive;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

@AutoJsonSerializer(target = {Character.class, char.class})
public class JsonCharSerializer implements JsonSerializer<Character> {
    @Override
    public void serialize(JSONObject json, String key, Character target) {
        json.put(key,
                 target
        );
    }

    @Override
    public Character deserialize(JSONObject json, String key) {
        return json.getString(key)
                   .charAt(0);
    }
}
