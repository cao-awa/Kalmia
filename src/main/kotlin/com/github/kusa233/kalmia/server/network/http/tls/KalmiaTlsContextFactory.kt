package com.github.kusa233.kalmia.server.network.http.tls

import com.github.kusa233.kalmia.server.network.http.tls.config.KalmiaTlsConfig
import io.netty.handler.ssl.ClientAuth
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.IOException
import java.security.KeyStore
import java.security.UnrecoverableKeyException
import javax.net.ssl.KeyManagerFactory

/**
 * Builds the [SslContext] of the Kalmia HTTPS listener.
 *
 * Netty is used without `netty-tcnative` and without BouncyCastle, so the JDK SSL
 * provider is active and only PKCS#8 private keys are accepted. PEM files are
 * therefore parsed here so that PKCS#1 (`BEGIN RSA PRIVATE KEY`) and SEC1
 * (`BEGIN EC PRIVATE KEY`) keys are converted to PKCS#8 before being handed to
 * Netty.
 */
internal object KalmiaTlsContextFactory {
    private val LOGGER: Logger = LogManager.getLogger("KalmiaTlsContextFactory")

    /**
     * Creates the server [SslContext] described by [config].
     *
     * @throws IllegalStateException when the certificate material is missing,
     *         unreadable or in an unsupported format.
     */
    fun create(config: KalmiaTlsConfig): SslContext {
        val format = config.resolveFormat()
        val certificateFile = config.certificateFile()

        if (!certificateFile.isFile) {
            throw IllegalStateException(
                "TLS certificate file '${certificateFile.absolutePath}' does not exist"
            )
        }

        val context = when (format) {
            KalmiaTlsCertificateFormat.PEM -> createFromPem(config, certificateFile)
            KalmiaTlsCertificateFormat.PKCS12 -> createFromKeystore(config, certificateFile, "PKCS12")
            KalmiaTlsCertificateFormat.JKS -> createFromKeystore(config, certificateFile, "JKS")
        }

        LOGGER.info("Kalmia TLS context created from {} certificate '{}'", format, certificateFile.absolutePath)

        return context
    }

    private fun createFromPem(config: KalmiaTlsConfig, certificateFile: File): SslContext {
        val certificateBlocks = KalmiaPemReader.readBlocks(certificateFile.readText(Charsets.UTF_8))
        val certificateChain = KalmiaPemReader.readCertificates(certificateBlocks)

        if (certificateChain.isEmpty()) {
            throw IllegalStateException(
                "No CERTIFICATE block found in '${certificateFile.absolutePath}'"
            )
        }

        // The key may live in the same file as the certificate chain, which is how
        // most single file PEM bundles are distributed.
        val keyFile = config.privateKeyFile()
        val keyBlocks = if (keyFile != null) {
            if (!keyFile.isFile) {
                throw IllegalStateException("TLS private key file '${keyFile.absolutePath}' does not exist")
            }
            KalmiaPemReader.readBlocks(keyFile.readText(Charsets.UTF_8))
        } else {
            certificateBlocks
        }

        val keyBlock = keyBlocks.firstOrNull { KalmiaPemReader.isPrivateKeyBlock(it.type) }
            ?: throw IllegalStateException(
                if (keyFile != null) {
                    "No private key block found in '${keyFile.absolutePath}'"
                } else {
                    "No private key block found in '${certificateFile.absolutePath}', " +
                            "please configure 'private_key' with the PEM private key path"
                }
            )

        val privateKey = KalmiaPemReader.readPrivateKey(keyBlock, config.passwordChars())

        LOGGER.info(
            "Loaded PEM private key '{}' ({}), algorithm {}",
            keyBlock.type,
            if (keyBlock.type == "PRIVATE KEY") "PKCS#8" else "converted to PKCS#8",
            privateKey.algorithm
        )

        return SslContextBuilder.forServer(privateKey, certificateChain)
            .clientAuth(ClientAuth.NONE)
            .build()
    }

    private fun createFromKeystore(
        config: KalmiaTlsConfig,
        keystoreFile: File,
        keystoreType: String
    ): SslContext {
        val password = config.passwordChars() ?: CharArray(0)
        val keyStore = KeyStore.getInstance(keystoreType)

        try {
            keystoreFile.inputStream().use { input ->
                keyStore.load(input, password)
            }
        } catch (exception: IOException) {
            // The JDK reports a wrong store password through an IOException, so the
            // message decides whether the file or the 'password' config is at fault.
            val hint = if (exception.message.orEmpty().contains("password", ignoreCase = true)) {
                "the 'password' of the tls config is wrong"
            } else {
                "the file is corrupted or it is not a $keystoreType keystore"
            }
            throw IllegalStateException(
                "Unable to load the $keystoreType keystore '${keystoreFile.absolutePath}', $hint: " +
                        exception.message,
                exception
            )
        }

        val keyManagerFactory = try {
            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).also {
                it.init(keyStore, password)
            }
        } catch (exception: UnrecoverableKeyException) {
            throw IllegalStateException(
                "Unable to read the private key of '$keystoreFile', the 'password' of the tls " +
                        "config is wrong",
                exception
            )
        }

        LOGGER.info("Loaded {} keystore '{}' with {} key entries", keystoreType, keystoreFile.absolutePath, keyStore.size())

        return SslContextBuilder.forServer(keyManagerFactory)
            .clientAuth(ClientAuth.NONE)
            .build()
    }
}
