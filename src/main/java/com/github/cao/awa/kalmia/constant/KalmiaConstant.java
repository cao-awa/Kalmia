package com.github.cao.awa.kalmia.constant;

import com.github.cao.awa.kalmia.protocol.RequestProtocol;

public class KalmiaConstant {
    public static final long PROTOCOL_VERSION = 1;
    public static final long PROTOCOL_COMPATIBLE_VERSION = 1;
    public static final RequestProtocol STANDARD_REQUEST_PROTOCOL = new RequestProtocol("KALMIA_STANDARD",
                                                                                        PROTOCOL_VERSION,
                                                                                        PROTOCOL_COMPATIBLE_VERSION,
                                                                                        false
    );

    public static final String SERVER_CONFIG_PATH = "configs/server-config.json";
    public static final String SERVER_DEFAULT_CONFIG_PATH = "kalmiagram/config/default-server-config.json";

    public static final String CLIENT_CONFIG_PATH = "configs/client-config.json";
    public static final String CLIENT_DEFAULT_CONFIG_PATH = "kalmiagram/config/default-client-config.json";
}
