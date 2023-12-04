package com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;
import com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap.network.ClientNetworkConfig;
import com.github.cao.awa.kalmia.config.kalmiagram.meta.BootstrapConfigMeta;

public class ClientBootstrapConfig extends ConfigElement {
    private final BootstrapConfigMeta meta;
    private final ClientNetworkConfig clientNetwork;

    public ClientBootstrapConfig(BootstrapConfigMeta meta, ClientNetworkConfig clientNetwork) {
        this.meta = meta;
        this.clientNetwork = clientNetwork;
    }

    public BootstrapConfigMeta meta() {
        return this.meta;
    }

    public ClientNetworkConfig clientNetwork() {
        return this.clientNetwork;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("config-meta",
                 this.meta.toJSON()
        );
        json.put("client-network",
                 this.clientNetwork.toJSON()
        );
        return json;
    }

    public static ClientBootstrapConfig read(JSONObject json, ClientBootstrapConfig compute) {
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

        ClientNetworkConfig serverNetwork = ClientNetworkConfig.read(
                subObject(json,
                          "client-network"
                ),
                compute.clientNetwork()
        );

        return new ClientBootstrapConfig(meta,
                                         serverNetwork
        );
    }
}
