package com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.network;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;

public class ServerNetworkConfig extends ConfigElement {
    private final String bindHost;
    private final int bindPort;
    private final boolean useEpoll;

    public ServerNetworkConfig(String bindHost, int bindPort, boolean useEpoll) {
        this.bindHost = bindHost;
        this.bindPort = bindPort;
        this.useEpoll = useEpoll;
    }

    public String bindHost() {
        return this.bindHost;
    }

    public int bindPort() {
        return this.bindPort;
    }

    public boolean useEpoll() {
        return this.useEpoll;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("bind-host",
                 this.bindHost
        );
        json.put("bind-port",
                 this.bindPort
        );
        json.put("use-epoll",
                 this.useEpoll
        );
        return json;
    }

    public static ServerNetworkConfig read(JSONObject json, ServerNetworkConfig compute) {
        if (compute == null) {
            throw new IllegalArgumentException("Compute argument cannot be null");
        }

        if (json == null) {
            return compute;
        }

        String bindHost = compute(json,
                                  "bind-host",
                                  compute :: bindHost
        );

        int bindPort = compute(json,
                               "bind-port",
                               compute :: bindPort
        );

        boolean useEpoll = compute(json,
                                   "use-epoll",
                                   compute :: useEpoll
        );

        return new ServerNetworkConfig(
                bindHost,
                bindPort,
                useEpoll
        );
    }
}
