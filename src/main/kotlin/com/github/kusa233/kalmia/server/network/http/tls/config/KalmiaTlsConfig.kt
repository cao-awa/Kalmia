package com.github.kusa233.kalmia.server.network.http.tls.config

import com.github.cao.awa.cason.obj.JSONObject
import com.github.kusa233.kalmia.config.KalmiaConfig
import com.github.kusa233.kalmia.server.network.http.tls.KalmiaTlsCertificateFormat
import java.io.File

/**
 * Configures the HTTPS support of the Kalmia HTTP server.
 *
 * HTTPS shares the socket of the plain HTTP listener: the protocol of a
 * connection is detected from its first bytes, so `http://host:12345` and
 * `https://host:12345` are both served by the same port configured with
 * `server_host` and `server_port` of the http config. This class therefore only
 * holds the certificate material.
 */
open class KalmiaTlsConfig: KalmiaConfig() {
    companion object {
        @JvmStatic
        fun createFromJSON(json: JSONObject): KalmiaTlsConfig {
            val config = KalmiaTlsConfig()
            json.ifBoolean("enable") {
                config.enable = this
            }
            json.ifString("certificate") {
                config.certificate = this
            }
            json.ifString("private_key") {
                config.privateKey = this
            }
            json.ifString("password") {
                config.password = this
            }
            json.ifString("format") {
                config.format = this
            }
            return config
        }
    }

    private var enable: Boolean = false
    private var certificate: String = ""
    private var privateKey: String = ""
    private var password: String = ""
    private var format: String = "auto"

    /**
     * Whether the shared HTTP port should also accept TLS connections.
     */
    fun enable(): Boolean {
        return this.enable
    }

    open fun enable(enable: Boolean): KalmiaTlsConfig {
        this.enable = enable
        return this
    }

    /**
     * Path of the certificate chain for [KalmiaTlsCertificateFormat.PEM], or the
     * keystore file for [KalmiaTlsCertificateFormat.PKCS12] and
     * [KalmiaTlsCertificateFormat.JKS].
     */
    fun certificate(): String {
        return this.certificate
    }

    open fun certificate(path: String): KalmiaTlsConfig {
        this.certificate = path
        return this
    }

    /**
     * Path of the PEM private key, unused by the keystore formats where the key
     * is stored inside [certificate].
     */
    fun privateKey(): String {
        return this.privateKey
    }

    open fun privateKey(path: String): KalmiaTlsConfig {
        this.privateKey = path
        return this
    }

    /**
     * Password of the keystore, or of the private key when it is encrypted.
     */
    fun password(): String {
        return this.password
    }

    open fun password(password: String): KalmiaTlsConfig {
        this.password = password
        return this
    }

    /**
     * Container format name, `auto` detects it from the certificate extension.
     */
    fun format(): String {
        return this.format
    }

    open fun format(format: String): KalmiaTlsConfig {
        this.format = format
        return this
    }

    /**
     * Resolves the certificate container format of this config.
     *
     * @throws IllegalStateException when no certificate is configured, or when
     *         the format cannot be detected from the file extension.
     */
    fun resolveFormat(): KalmiaTlsCertificateFormat {
        val certificatePath = this.certificate.trim()
        if (certificatePath.isEmpty()) {
            throw IllegalStateException(
                "TLS is enabled but 'certificate' is empty, please set the certificate path in the tls config"
            )
        }

        val explicit = this.format.trim()
        if (!explicit.equals("auto", ignoreCase = true)) {
            return KalmiaTlsCertificateFormat.parse(explicit)
                ?: throw IllegalStateException(
                    "Unknown TLS certificate format '${this.format}', " +
                            "expected one of auto, pem, pkcs12 or jks"
                )
        }

        return KalmiaTlsCertificateFormat.detect(certificatePath)
            ?: throw IllegalStateException(
                "Cannot detect the TLS certificate format from '${certificatePath}', " +
                        "please set the 'format' entry to auto, pem, pkcs12 or jks"
            )
    }

    /**
     * Absolute file of [certificate].
     */
    fun certificateFile(): File {
        return File(this.certificate.trim())
    }

    /**
     * Absolute file of [privateKey], `null` when no separate key file is configured.
     */
    fun privateKeyFile(): File? {
        val path = this.privateKey.trim()
        return if (path.isEmpty()) null else File(path)
    }

    /**
     * Char array of [password], `null` when no password is configured.
     */
    fun passwordChars(): CharArray? {
        val value = this.password
        return if (value.isEmpty()) null else value.toCharArray()
    }

    override fun toJSON(): JSONObject {
        return JSONObject {
            "enable" set enable
            "certificate" set certificate
            "private_key" set privateKey
            "password" set password
            "format" set format
        }
    }
}
