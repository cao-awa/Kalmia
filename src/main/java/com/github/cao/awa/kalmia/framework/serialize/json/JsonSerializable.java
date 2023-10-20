package com.github.cao.awa.kalmia.framework.serialize.json;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotation.auto.Auto;

public interface JsonSerializable<T> {
    @Auto
    JSONObject serialize();

    @Auto
    T deserialize(JSONObject json);
}
