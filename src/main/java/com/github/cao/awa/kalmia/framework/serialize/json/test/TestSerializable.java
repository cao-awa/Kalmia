package com.github.cao.awa.kalmia.framework.serialize.json.test;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializable;

public class TestSerializable implements JsonSerializable<TestSerializable> {
    private String testAwa;

    public TestSerializable(String testAwa) {
        this.testAwa = testAwa;
    }

    @Auto
    public TestSerializable() {

    }

    @Override
    public JSONObject serialize() {
        JSONObject json = new JSONObject();
        json.put("test",
                 this.testAwa
        );
        return json;
    }

    @Override
    public TestSerializable deserialize(JSONObject json) {
        this.testAwa = json.getString("test");
        return this;
    }
}
