package com.github.cao.awa.kalmia.config.kalmiagram.meta.network;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;
import com.github.cao.awa.kalmia.config.kalmiagram.meta.ConfigMeta;

public class RouterNetworkConfig extends ConfigElement {
    private final ConfigMeta meta;
    private final int compressThreshold;

    public RouterNetworkConfig(ConfigMeta meta, int compressThreshold) {
        this.meta = meta;
        this.compressThreshold = compressThreshold;
    }

    public ConfigMeta meta() {
        return this.meta;
    }

    public int compressThreshold() {
        return this.compressThreshold;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("config-meta",
                 this.meta.toJSON()
        );
        json.put("compress-threshold",
                 this.compressThreshold
        );
        return json;
    }

    public static RouterNetworkConfig read(JSONObject json, RouterNetworkConfig compute) {
        if (compute == null) {
            throw new IllegalArgumentException("Compute argument cannot be null");
        }

        if (json == null) {
            return compute;
        }

        ConfigMeta meta = ConfigMeta.read(
                subObject(json,
                          "config-meta"
                ),
                compute.meta()
        );

        int compressThreshold = compute(json,
                                        "compress-threshold",
                                        compute :: compressThreshold
        );

        return new RouterNetworkConfig(
                meta,
                compressThreshold
        );
    }
}
