package com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.translation;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;

public class BootstrapTranslationConfig extends ConfigElement {
    private final boolean enable;

    public BootstrapTranslationConfig(boolean enable) {
        this.enable = enable;
    }

    public boolean enable() {
        return this.enable;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("enable",
                 this.enable
        );
        return json;
    }

    public static BootstrapTranslationConfig read(JSONObject json, BootstrapTranslationConfig compute) {
        if (compute == null) {
            throw new IllegalArgumentException("Compute argument cannot be null");
        }

        if (json == null) {
            return compute;
        }

        boolean enable = compute(json,
                                 "enable",
                                 compute :: enable
        );

        return new BootstrapTranslationConfig(enable);
    }
}
