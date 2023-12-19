package com.github.cao.awa.kalmia.constant

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.protocol.RequestProtocol
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment

object KalmiaConstant {
    const val PROTOCOL_VERSION: Long = 1
    const val PROTOCOL_COMPATIBLE_VERSION: Long = 1

    @JvmField
    val STANDARD_REQUEST_PROTOCOL = RequestProtocol(
        "KALMIA_STANDARD",
        PROTOCOL_VERSION,
        PROTOCOL_COMPATIBLE_VERSION,
        false
    )

    const val BUILD_NAME = "kalmia-main-8ecf12de"

    const val SERVER_CONFIG_PATH = "configs/server-config.json"
    const val SERVER_DEFAULT_CONFIG_PATH = "kalmiagram/config/default-server-config.json"
    const val CLIENT_CONFIG_PATH = "configs/client-config.json"
    const val CLIENT_DEFAULT_CONFIG_PATH = "kalmiagram/config/default-client-config.json"

    const val KEYPAIR_STORAGE_PATH = "ciphers/"
    const val MAIN_KEYPAIR_META_PATH = "ciphers/main/cipher.json"
    const val MAIN_PRIVATE_KEY_PATH = "ciphers/main/SECRET_PRIVATE"
    const val MAIN_PUBLIC_KEY_PATH = "ciphers/main/SECRET_PUBLIC"

    const val LANGUAGE_TRANSLATION_RESOURCE_PATH = "resources/lang/"
    const val LANGUAGE_TRANSLATION_MAIN_RESOURCE_PATH = "resources/lang/main"

    @JvmField
    val LANGUAGE_TRANSLATION_DEFAULT_RESOURCES: Map<String, String> = EntrustEnvironment.operation(
        ApricotCollectionFactor.hashMap()
    ) { map ->
        map["en_us"] = "kalmiagram/lang/en_us.json"
        map["zh_cn"] = "kalmiagram/lang/zh_cn.json"
    }

    @JvmField
    val UNMARKED_PURE_IDENTITY = PureExtraIdentity.create(byteArrayOf(-1))

    @JvmField
    val UNMARKED_LONG_AND_EXTRA_IDENTITY = LongAndExtraIdentity.create(-1, byteArrayOf(-1))
}