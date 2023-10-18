package com.github.cao.awa.kalmia.config.kalmiagram.bootstrap;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;
import com.github.cao.awa.kalmia.config.kalmiagram.bootstrap.meta.BootstrapConfigMeta;
import com.github.cao.awa.kalmia.config.kalmiagram.bootstrap.meta.ServerNetworkConfig;
import com.github.cao.awa.kalmia.config.kalmiagram.bootstrap.translation.BootstrapTranslationConfig;

public class ServerBootstrapConfig extends ConfigElement {
    private final BootstrapConfigMeta meta;
    private final ServerNetworkConfig serverNetwork;
    private final BootstrapTranslationConfig translation;

    public ServerBootstrapConfig(BootstrapConfigMeta meta, ServerNetworkConfig serverNetwork, BootstrapTranslationConfig translation) {
        this.meta = meta;
        this.serverNetwork = serverNetwork;
        this.translation = translation;
    }

    public BootstrapConfigMeta meta() {
        return this.meta;
    }

    public ServerNetworkConfig serverNetwork() {
        return this.serverNetwork;
    }

    public BootstrapTranslationConfig translation() {
        return this.translation;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("config-meta",
                 this.meta.toJSON()
        );
        json.put("server-network",
                 this.serverNetwork.toJSON()
        );
        json.put("translation",
                 this.translation.toJSON()
        );
        return json;
    }

    public static ServerBootstrapConfig read(JSONObject json, ServerBootstrapConfig compute) {
        if (compute == null) {
            throw new IllegalArgumentException("Compute argument cannot be null");
        }

        if (json == null) {
            return compute;
        }

        BootstrapConfigMeta meta = BootstrapConfigMeta.read(
                subObject(json,
                          "config-meta"
                ),
                compute.meta()
        );

        ServerNetworkConfig serverNetwork = ServerNetworkConfig.read(
                subObject(json,
                          "server-network"
                ),
                compute.serverNetwork()
        );

        BootstrapTranslationConfig translation = BootstrapTranslationConfig.read(
                subObject(json,
                          "translation"
                ),
                compute.translation()
        );

        return new ServerBootstrapConfig(meta,
                                         serverNetwork,
                                         translation
        );
    }
}
