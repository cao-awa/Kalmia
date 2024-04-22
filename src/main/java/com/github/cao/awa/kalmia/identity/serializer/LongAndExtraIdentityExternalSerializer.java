package com.github.cao.awa.kalmia.identity.serializer;


import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;

@AutoJsonSerializer(target = {LongAndExtraIdentity.class})
public class LongAndExtraIdentityExternalSerializer implements JsonSerializer<LongAndExtraIdentity> {
    @Override
    public void serialize(JSONObject json, String key, LongAndExtraIdentity target) {
        json.put(key,
                 target.toJSON()
        );
    }

    @Override
    public LongAndExtraIdentity deserialize(JSONObject json, String key) {
        return LongAndExtraIdentity.create(json.getJSONObject(key));
    }
}
