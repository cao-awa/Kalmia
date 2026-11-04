package com.github.kusa233.kalmia.server.network.http.tls

/**
 * Supported certificate container formats for the Kalmia HTTPS server.
 *
 * The format is resolved from the `format` config entry, and when that entry is
 * `auto` (the default) it is detected from the certificate file extension.
 */
enum class KalmiaTlsCertificateFormat {
    /** PEM encoded certificate chain plus a PEM encoded private key. */
    PEM,

    /** PKCS#12 container, the default keystore type of the JDK. */
    PKCS12,

    /** Legacy Java keystore format. */
    JKS;

    companion object {
        private val PEM_EXTENSIONS: Set<String> = setOf("pem", "crt", "cer", "cert")
        private val PKCS12_EXTENSIONS: Set<String> = setOf("p12", "pfx", "pkcs12")
        private val JKS_EXTENSIONS: Set<String> = setOf("jks", "keystore")

        /**
         * Detects the container format from a file name, returns `null` when the
         * extension is not a known certificate container extension.
         */
        @JvmStatic
        fun detect(fileName: String): KalmiaTlsCertificateFormat? {
            val extension = fileName.substringAfterLast('.', "").lowercase()
            if (extension.isEmpty()) {
                return null
            }
            return when (extension) {
                in PEM_EXTENSIONS -> PEM
                in PKCS12_EXTENSIONS -> PKCS12
                in JKS_EXTENSIONS -> JKS
                else -> null
            }
        }

        /**
         * Parses an explicit config value, returns `null` when the name is unknown.
         */
        @JvmStatic
        fun parse(name: String): KalmiaTlsCertificateFormat? {
            return when (name.trim().lowercase()) {
                "pem" -> PEM
                "pkcs12", "p12", "pfx" -> PKCS12
                "jks" -> JKS
                else -> null
            }
        }
    }
}
