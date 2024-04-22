package com.github.cao.awa.kalmia.identity.serializer;


import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.mathematic.Mathematics;

@AutoJsonSerializer(target = {PureExtraIdentity.class})
public class PureExtraIdentityExternalSerializer implements JsonSerializer<PureExtraIdentity> {
    @Override
    public void serialize(JSONObject json, String key, PureExtraIdentity target) {
        json.put(key,
                 Mathematics.radix(
                         target.extras(),
                         36
                 )
        );
    }

    @Override
    public PureExtraIdentity deserialize(JSONObject json, String key) {
        return PureExtraIdentity.create(
                Mathematics.toBytes(
                        json.getString(key),
                        36
                )
        );
    }
}
