package com.github.cao.awa.kalmia.config.kalmiagram.bootstrap.translation;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;

public class BootstrapTranslationConfig extends ConfigElement {
    private final boolean enable;
    private final String serverHost;
    private final int serverPort;

    public BootstrapTranslationConfig(boolean enable, String serverHost, int serverPort) {
        this.enable = enable;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public boolean enable() {
        return this.enable;
    }

    public String serverHost() {
        return this.serverHost;
    }

    public int serverPort() {
        return this.serverPort;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("enable",
                 this.enable
        );
        json.put("server-host",
                 this.serverHost
        );
        json.put("server-port",
                 this.serverPort
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

        String serverHost = compute(json,
                                    "server-host",
                                    compute :: serverHost
        );

        int serverPort = compute(json,
                                 "server-port",
                                 compute :: serverPort
        );

        return new BootstrapTranslationConfig(enable,
                                              serverHost,
                                              serverPort
        );
    }
}
