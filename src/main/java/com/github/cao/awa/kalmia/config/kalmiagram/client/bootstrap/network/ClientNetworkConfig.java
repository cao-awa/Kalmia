package com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap.network;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.config.ConfigElement;

public class ClientNetworkConfig extends ConfigElement {
    private final String connectHost;
    private final int connectPort;
    private final boolean useEpoll;

    public ClientNetworkConfig(String connectHost, int connectPort, boolean useEpoll) {
        this.connectHost = connectHost;
        this.connectPort = connectPort;
        this.useEpoll = useEpoll;
    }

    public String connectHost() {
        return this.connectHost;
    }

    public int connectPort() {
        return this.connectPort;
    }

    public boolean useEpoll() {
        return this.useEpoll;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("connect-host",
                 this.connectHost
        );
        json.put("connect-port",
                 this.connectPort
        );
        json.put("use-epoll",
                 this.useEpoll
        );
        return json;
    }

    public static ClientNetworkConfig read(JSONObject json, ClientNetworkConfig compute) {
        if (compute == null) {
            throw new IllegalArgumentException("Compute argument cannot be null");
        }

        if (json == null) {
            return compute;
        }

        String bindHost = compute(json,
                                  "connect-host",
                                  compute :: connectHost
        );

        int bindPort = compute(json,
                               "connect-port",
                               compute :: connectPort
        );

        boolean useEpoll = compute(json,
                                   "use-epoll",
                                   compute :: useEpoll
        );

        return new ClientNetworkConfig(
                bindHost,
                bindPort,
                useEpoll
        );
    }
}
