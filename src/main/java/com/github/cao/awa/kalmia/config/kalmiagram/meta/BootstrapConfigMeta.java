package com.github.cao.awa.kalmia.config.kalmiagram.meta;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;

public class BootstrapConfigMeta extends ConfigElement {
    private final int version;

    public BootstrapConfigMeta(int version) {
        this.version = version;
    }

    public int version() {
        return this.version;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("version",
                 this.version
        );
        return json;
    }

    public static BootstrapConfigMeta read(JSONObject json, BootstrapConfigMeta compute) {
        if (compute == null) {
            throw new IllegalArgumentException("Compute argument cannot be null");
        }

        if (json == null) {
            return compute;
        }

        int version = compute(json,
                              "version",
                              compute :: version
        );

        version = Math.max(
                version,
                compute.version()
        );

        return new BootstrapConfigMeta(version);
    }
}
