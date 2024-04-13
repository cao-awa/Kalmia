package com.github.cao.awa.kalmia.env.security.key

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import com.github.cao.awa.apricot.util.encryption.Crypto
import com.github.cao.awa.kalmia.mathematic.Mathematics
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.util.*

object PreSharedCipherEncode {
    @JvmStatic
    fun encodeEcPrivate(privateKey: ECPrivateKey, useBase64: Boolean): String {
        val data = if (useBase64) Base64.getEncoder().encodeToString(privateKey.encoded)
        else Mathematics.radix(
            privateKey.encoded, 36
        )

        val json = JSONObject()

        json["key-type"] = "private-ec"
        json["encode-mode"] = if (useBase64) "base64" else "kalmia-base36"
        json["data"] = data

        return json.toString(JSONWriter.Feature.PrettyFormat)
    }

    @JvmStatic
    fun decodeEcPrivate(data: JSONObject): ECPrivateKey {
        if (data["key-type"].equals("private-ec")) {
            val mode = data["encode-mode"]

            val keyData = if (mode.equals("base64")) {
                Base64.getDecoder().decode(data.getString("data"))
            } else {
                Mathematics.toBytes(data.getString("data"), 36)
            }

            return Crypto.decodeEcPrikey(keyData)
        }
        throw IllegalArgumentException("The data are not ec private key data")
    }

    @JvmStatic
    fun encodeEcPublic(publicKey: ECPublicKey, useBase64: Boolean): String {
        val data = if (useBase64) Base64.getEncoder().encodeToString(publicKey.encoded)
        else Mathematics.radix(
            publicKey.encoded, 36
        )

        val json = JSONObject()

        json["key-type"] = "public-ec"
        json["encode-mode"] = if (useBase64) "base64" else "kalmia-base36"
        json["data"] = data

        return json.toString(JSONWriter.Feature.PrettyFormat)
    }

    @JvmStatic
    fun decodeEcPublic(data: JSONObject): ECPublicKey {
        if (data["key-type"].equals("public-ec")) {
            val mode = data["encode-mode"]

            val keyData = if (mode.equals("base64")) {
                Base64.getDecoder().decode(data.getString("data"))
            } else {
                Mathematics.toBytes(data.getString("data"), 36)
            }

            return Crypto.decodeEcPubkey(keyData)
        }
        throw IllegalArgumentException("The data are not ec public key data")
    }
}