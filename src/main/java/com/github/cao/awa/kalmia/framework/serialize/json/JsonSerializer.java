package com.github.cao.awa.kalmia.framework.serialize.json;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

@Auto
public interface JsonSerializer<T> {
    @Auto
    void serialize(JSONObject json, String key, T target);

    @Auto
    T deserialize(JSONObject json, String key);

    default JSONObject getObject(T target) {
        JSONObject json = new JSONObject();
        serialize(json,
                  "key",
                  target
        );
        return json.getJSONObject("key");
    }

    default JSONArray getArray(T target) {
        JSONObject json = new JSONObject();
        serialize(json,
                  "key",
                  target
        );
        return json.getJSONArray("key");
    }

    default Class<T>[] target() {
        return EntrustEnvironment.cast(KalmiaEnv.JSON_SERIALIZE_FRAMEWORK.target(this));
    }
}
