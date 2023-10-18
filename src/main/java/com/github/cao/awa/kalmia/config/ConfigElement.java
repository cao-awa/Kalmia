package com.github.cao.awa.kalmia.config;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ConfigElement {
    public abstract JSONObject toJSON();

    @SuppressWarnings("unchecked")
    public static <T> T compute(JSONObject json, String key, Supplier<T> compute) {
        return (T) Optional.ofNullable(json.get(key))
                           .orElseGet(compute);
    }

    public static JSONObject subObject(JSONObject json, String key) {
        return EntrustEnvironment.result(json,
                                         jsonObject -> jsonObject.getJSONObject(key)
        );
    }

    public static JSONArray subArray(JSONObject json, String key) {
        return EntrustEnvironment.result(json,
                                         jsonObject -> jsonObject.getJSONArray(key)
        );
    }
}
