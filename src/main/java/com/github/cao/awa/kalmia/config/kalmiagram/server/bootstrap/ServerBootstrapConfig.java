package com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;
import com.github.cao.awa.kalmia.config.kalmiagram.meta.ConfigMeta;
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.network.ServerNetworkConfig;
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.translation.BootstrapTranslationConfig;

public class ServerBootstrapConfig extends ConfigElement {
    private final ConfigMeta meta;
    private final ServerNetworkConfig serverNetwork;
    private final BootstrapTranslationConfig translation;
    private final String serverName;

    public ServerBootstrapConfig(ConfigMeta meta, ServerNetworkConfig serverNetwork, BootstrapTranslationConfig translation, String serverName) {
        this.meta = meta;
        this.serverNetwork = serverNetwork;
        this.translation = translation;
        this.serverName = serverName;
    }

    public ConfigMeta meta() {
        return this.meta;
    }

    public ServerNetworkConfig serverNetwork() {
        return this.serverNetwork;
    }

    public BootstrapTranslationConfig translation() {
        return this.translation;
    }

    public String serverName() {
        return this.serverName;
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
        json.put("server-name",
                 this.serverName
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

        ConfigMeta meta = ConfigMeta.read(
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

        String serverName = compute(json,
                                    "server-name",
                                    compute :: serverName
        );

        return new ServerBootstrapConfig(meta,
                                         serverNetwork,
                                         translation,
                                         serverName
        );
    }
}
