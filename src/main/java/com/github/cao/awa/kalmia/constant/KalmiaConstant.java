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
}
