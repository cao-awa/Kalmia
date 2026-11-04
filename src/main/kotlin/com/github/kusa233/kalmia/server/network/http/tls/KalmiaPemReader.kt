package com.github.kusa233.kalmia.server.network.http.tls

import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

/**
 * Minimal DER/PEM reader used to load TLS private keys and certificate chains
 * without any extra dependency.
 *
 * Netty's JDK SSL provider only understands PKCS#8 encoded private keys, while
 * the most common PEM files produced by OpenSSL (`openssl genrsa`) contain a
 * PKCS#1 `BEGIN RSA PRIVATE KEY` block and EC keys are usually written as SEC1
 * `BEGIN EC PRIVATE KEY` blocks. Both layouts are converted into PKCS#8 here so
 * that ordinary PEM files work out of the box.
 */
internal object KalmiaPemReader {
    private const val PEM_CERTIFICATE: String = "CERTIFICATE"
    private const val PEM_X509_CERTIFICATE: String = "X509 CERTIFICATE"
    private const val PEM_TRUSTED_CERTIFICATE: String = "TRUSTED CERTIFICATE"
    private const val PEM_PRIVATE_KEY: String = "PRIVATE KEY"
    private const val PEM_ENCRYPTED_PRIVATE_KEY: String = "ENCRYPTED PRIVATE KEY"
    private const val PEM_RSA_PRIVATE_KEY: String = "RSA PRIVATE KEY"
    private const val PEM_EC_PRIVATE_KEY: String = "EC PRIVATE KEY"

    private const val TAG_INTEGER: Int = 0x02
    private const val TAG_BIT_STRING: Int = 0x03
    private const val TAG_OCTET_STRING: Int = 0x04
    private const val TAG_NULL: Int = 0x05
    private const val TAG_OID: Int = 0x06
    private const val TAG_SEQUENCE: Int = 0x30

    /** DER encoding of the rsaEncryption object identifier (1.2.840.113549.1.1.1). */
    private val OID_RSA_ENCRYPTION: ByteArray = byteArrayOf(
        0x06, 0x09,
        0x2A, 0x86.toByte(), 0x48, 0x86.toByte(), 0xF7.toByte(), 0x0D, 0x01, 0x01, 0x01
    )

    /** DER encoding of the ecPublicKey object identifier (1.2.840.10045.2.1). */
    private val OID_EC_PUBLIC_KEY: ByteArray = byteArrayOf(
        0x06, 0x07,
        0x2A, 0x86.toByte(), 0x48, 0xCE.toByte(), 0x3D, 0x02, 0x01
    )

    /** DER encoding of the dsa object identifier (1.2.840.10040.4.1). */
    private val OID_DSA: ByteArray = byteArrayOf(
        0x06, 0x07,
        0x2A, 0x86.toByte(), 0x48, 0xCE.toByte(), 0x38, 0x04, 0x01
    )

    /** DER encoding of the Ed25519 object identifier (1.3.101.112). */
    private val OID_ED25519: ByteArray = byteArrayOf(
        0x06, 0x03,
        0x2B, 0x65, 0x70
    )

    /** DER encoding of the Ed448 object identifier (1.3.101.113). */
    private val OID_ED448: ByteArray = byteArrayOf(
        0x06, 0x03,
        0x2B, 0x65, 0x71
    )

    internal class PemBlock(val type: String, val der: ByteArray)

    /**
     * Splits PEM text into its `BEGIN`/`END` blocks, ignoring any surrounding
     * text such as the bag attributes OpenSSL prints in front of a key.
     */
    fun readBlocks(pem: String): List<PemBlock> {
        val blocks = mutableListOf<PemBlock>()
        val beginMarker = "-----BEGIN "
        val endMarker = "-----END "

        var cursor = 0
        while (true) {
            val begin = pem.indexOf(beginMarker, cursor)
            if (begin < 0) {
                break
            }
            val typeEnd = pem.indexOf("-----", begin + beginMarker.length)
            if (typeEnd < 0) {
                break
            }
            val type = pem.substring(begin + beginMarker.length, typeEnd).trim()
            val bodyStart = typeEnd + "-----".length
            val end = pem.indexOf(endMarker, bodyStart)
            if (end < 0) {
                break
            }
            val body = pem.substring(bodyStart, end)
            cursor = end + endMarker.length

            val decoded = try {
                Base64.getMimeDecoder().decode(body.replace(Regex("\\s"), ""))
            } catch (_: IllegalArgumentException) {
                continue
            }
            if (decoded.isNotEmpty()) {
                blocks.add(PemBlock(type.uppercase(), decoded))
            }
        }

        return blocks
    }

    fun isCertificateBlock(type: String): Boolean {
        return type == PEM_CERTIFICATE || type == PEM_X509_CERTIFICATE || type == PEM_TRUSTED_CERTIFICATE
    }

    fun isPrivateKeyBlock(type: String): Boolean {
        return type == PEM_PRIVATE_KEY ||
                type == PEM_RSA_PRIVATE_KEY ||
                type == PEM_EC_PRIVATE_KEY ||
                type == PEM_ENCRYPTED_PRIVATE_KEY
    }

    fun isEncryptedBlock(type: String): Boolean {
        return type == PEM_ENCRYPTED_PRIVATE_KEY
    }

    /**
     * Converts any supported PEM private key block into a usable [PrivateKey].
     *
     * @param block a block whose type passed [isPrivateKeyBlock].
     * @param password unused unless the block is encrypted, which is rejected
     *        because Netty's JDK provider cannot decrypt PEM containers.
     */
    fun readPrivateKey(block: PemBlock, password: CharArray?): PrivateKey {
        if (isEncryptedBlock(block.type)) {
            throw IllegalStateException(
                "Encrypted PEM private keys ('${block.type}') are not supported, " +
                        "please decrypt it with 'openssl pkcs8 -topk8 -nocrypt' or use a PKCS#12 keystore"
            )
        }
        if (password != null && password.isNotEmpty() && block.type == PEM_PRIVATE_KEY) {
            // A PKCS#8 block is not encrypted, the password simply does not apply.
        }

        return when (block.type) {
            PEM_PRIVATE_KEY -> decodePkcs8(block.der)
            PEM_RSA_PRIVATE_KEY -> decodePkcs8(wrapPkcs1InPkcs8(block.der, OID_RSA_ENCRYPTION))
            PEM_EC_PRIVATE_KEY -> decodePkcs8(wrapSec1InPkcs8(block.der))
            else -> throw IllegalStateException("Unsupported PEM private key type '${block.type}'")
        }
    }

    /**
     * Builds the X.509 certificates of a PEM chain, preserving file order.
     */
    fun readCertificates(blocks: List<PemBlock>): List<java.security.cert.X509Certificate> {
        val factory = java.security.cert.CertificateFactory.getInstance("X.509")
        return blocks
            .filter { isCertificateBlock(it.type) }
            .map { block ->
                factory.generateCertificate(block.der.inputStream()) as java.security.cert.X509Certificate
            }
    }

    private fun decodePkcs8(der: ByteArray): PrivateKey {
        val spec = PKCS8EncodedKeySpec(der)
        // Pick the KeyFactory from the AlgorithmIdentifier OID of the PKCS#8 header
        // instead of guess-iterating, so a corrupt key reports its real cause.
        val algorithm = readPkcs8Algorithm(der) ?: throw IllegalStateException(
            "Unable to read the PKCS#8 private key header, the key data looks corrupted"
        )

        return try {
            KeyFactory.getInstance(algorithm).generatePrivate(spec)
        } catch (exception: Exception) {
            throw IllegalStateException(
                "Unable to decode the PKCS#8 private key as '${algorithm}': ${exception.message}",
                exception
            )
        }
    }

    /**
     * Maps the PKCS#8 `AlgorithmIdentifier` OID to a JCA algorithm name, returns
     * `null` when the structure cannot be walked or the OID is unknown.
     */
    private fun readPkcs8Algorithm(der: ByteArray): String? {
        val outer = readTlv(der, 0) ?: return null
        val version = readTlv(der, outer.contentStart) ?: return null
        val algorithmSequence = readTlv(der, version.contentEnd) ?: return null
        val oid = readTlv(der, algorithmSequence.contentStart) ?: return null
        if ((der[algorithmSequence.contentStart].toInt() and 0xFF) != TAG_OID) {
            return null
        }

        val encoded = der.copyOfRange(algorithmSequence.contentStart, oid.contentEnd)
        return when {
            encoded.contentEquals(OID_RSA_ENCRYPTION) -> "RSA"
            encoded.contentEquals(OID_EC_PUBLIC_KEY) -> "EC"
            encoded.contentEquals(OID_DSA) -> "DSA"
            encoded.contentEquals(OID_ED25519) -> "Ed25519"
            encoded.contentEquals(OID_ED448) -> "Ed448"
            else -> null
        }
    }

    /**
     * Wraps a PKCS#1 `RSAPrivateKey` DER structure into a PKCS#8 `PrivateKeyInfo`.
     */
    private fun wrapPkcs1InPkcs8(pkcs1: ByteArray, algorithmOid: ByteArray): ByteArray {
        // PrivateKeyInfo ::= SEQUENCE { version INTEGER (0), algorithm AlgorithmIdentifier, privateKey OCTET STRING }
        // AlgorithmIdentifier ::= SEQUENCE { algorithm OID, parameters NULL }
        val algorithmIdentifier = derSequence(algorithmOid + derNull())
        val privateKeyInfo = derSequence(
            derInteger(0) + algorithmIdentifier + derOctetString(pkcs1)
        )
        return privateKeyInfo
    }

    /**
     * Wraps a SEC1 `ECPrivateKey` DER structure into a PKCS#8 `PrivateKeyInfo`.
     *
     * SEC1 already carries the curve parameters in an optional `[0]` field, which
     * is lifted into the PKCS#8 `AlgorithmIdentifier` parameters slot.
     */
    private fun wrapSec1InPkcs8(sec1: ByteArray): ByteArray {
        val parameters = readSec1CurveParameters(sec1)
            ?: throw IllegalStateException(
                "The EC private key does not embed its curve parameters, " +
                        "please provide a PKCS#8 or PKCS#12 key instead"
            )

        // AlgorithmIdentifier ::= SEQUENCE { algorithm OID (ecPublicKey), parameters ANY }
        val algorithmIdentifier = derSequence(OID_EC_PUBLIC_KEY + parameters)
        return derSequence(
            derInteger(0) + algorithmIdentifier + derOctetString(sec1)
        )
    }

    /**
     * Extracts the raw DER of the `[0] parameters` field of a SEC1 EC private key.
     */
    private fun readSec1CurveParameters(sec1: ByteArray): ByteArray? {
        val outer = readTlv(sec1, 0) ?: return null
        var cursor = outer.contentStart
        while (cursor < outer.contentEnd) {
            val tlv = readTlv(sec1, cursor) ?: return null
            val tag = sec1[cursor].toInt() and 0xFF
            if (tag == 0xA0) {
                // Context specific [0] EXPLICIT, the content is the curve OID.
                return sec1.copyOfRange(tlv.contentStart, tlv.contentEnd)
            }
            cursor = tlv.contentEnd
        }
        return null
    }

    private class Tlv(val contentStart: Int, val contentEnd: Int)

    private fun readTlv(source: ByteArray, offset: Int): Tlv? {
        if (offset >= source.size) {
            return null
        }
        var cursor = offset + 1 // Skip the tag byte.
        if (cursor >= source.size) {
            return null
        }
        var length = source[cursor].toInt() and 0xFF
        cursor++
        if (length and 0x80 != 0) {
            val lengthBytes = length and 0x7F
            if (lengthBytes == 0 || lengthBytes > 4) {
                return null
            }
            length = 0
            for (i in 0 until lengthBytes) {
                if (cursor >= source.size) {
                    return null
                }
                length = (length shl 8) or (source[cursor].toInt() and 0xFF)
                cursor++
            }
        }
        val end = cursor + length
        if (end < cursor || end > source.size) {
            return null
        }
        return Tlv(cursor, end)
    }

    private fun encodeLength(length: Int): ByteArray {
        if (length < 0x80) {
            return byteArrayOf(length.toByte())
        }
        var value = length
        val bytes = ByteArrayOutputStream()
        while (value > 0) {
            bytes.write(value and 0xFF)
            value = value shr 8
        }
        val littleEndian = bytes.toByteArray()
        val result = ByteArray(littleEndian.size + 1)
        result[0] = (0x80 or littleEndian.size).toByte()
        for (i in littleEndian.indices) {
            result[i + 1] = littleEndian[littleEndian.size - 1 - i]
        }
        return result
    }

    private fun derSequence(content: ByteArray): ByteArray {
        return byteArrayOf(TAG_SEQUENCE.toByte()) + encodeLength(content.size) + content
    }

    private fun derOctetString(content: ByteArray): ByteArray {
        return byteArrayOf(TAG_OCTET_STRING.toByte()) + encodeLength(content.size) + content
    }

    private fun derInteger(value: Int): ByteArray {
        val content = when {
            value < 0x80 -> byteArrayOf(value.toByte())
            value < 0x8000 -> byteArrayOf((value shr 8).toByte(), value.toByte())
            else -> byteArrayOf((value shr 16).toByte(), (value shr 8).toByte(), value.toByte())
        }
        return byteArrayOf(TAG_INTEGER.toByte()) + encodeLength(content.size) + content
    }

    private fun derNull(): ByteArray {
        return byteArrayOf(TAG_NULL.toByte(), 0x00)
    }
}
