package com.github.cao.awa.kalmia.constant;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Map;

public class KalmiaConstant {
    public static final long PROTOCOL_VERSION = 1;
    public static final long PROTOCOL_COMPATIBLE_VERSION = 1;
    public static final RequestProtocol STANDARD_REQUEST_PROTOCOL = new RequestProtocol("KALMIA_STANDARD",
                                                                                        PROTOCOL_VERSION,
                                                                                        PROTOCOL_COMPATIBLE_VERSION,
                                                                                        false
    );

    public static final String BUILD_NAME = "kalmia-main-8ecf12de";

    public static final String SERVER_CONFIG_PATH = "configs/server-config.json";
    public static final String SERVER_DEFAULT_CONFIG_PATH = "kalmiagram/config/default-server-config.json";

    public static final String CLIENT_CONFIG_PATH = "configs/client-config.json";
    public static final String CLIENT_DEFAULT_CONFIG_PATH = "kalmiagram/config/default-client-config.json";

    public static final String KEYPAIR_STORAGE_PATH = "ciphers/";
    public static final String MAIN_KEYPAIR_META_PATH = "ciphers/main/cipher.json";
    public static final String MAIN_PRIVATE_KEY_PATH = "ciphers/main/SECRET_PRIVATE";
    public static final String MAIN_PUBLIC_KEY_PATH = "ciphers/main/SECRET_PUBLIC";

    public static final String LANGUAGE_TRANSLATION_RESOURCE_PATH = "resources/lang/";
    public static final String LANGUAGE_TRANSLATION_MAIN_RESOURCE_PATH = "resources/lang/main";
    public static final Map<String, String> LANGUAGE_TRANSLATION_DEFAULT_RESOURCES = EntrustEnvironment.operation(
            ApricotCollectionFactor.hashMap(),
            map -> {
                map.put("en_us",
                        "kalmiagram/lang/en_us.json"
                );
                map.put("zh_cn",
                        "kalmiagram/lang/zh_cn.json"
                );
            }
    );
}
