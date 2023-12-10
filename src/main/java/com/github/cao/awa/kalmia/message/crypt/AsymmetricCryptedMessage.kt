package com.github.cao.awa.kalmia.message.crypt

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.apricot.util.digger.MessageDigger.Sha3
import com.github.cao.awa.apricot.util.encryption.Crypto
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.ClientMessageContent
import com.github.cao.awa.kalmia.message.identity.MessageIdentity
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.nio.charset.StandardCharsets

class AsymmetricCryptedMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(2)

        @JvmStatic
        fun create(reader: BytesReader): AsymmetricCryptedMessage? {
            return if (reader.read().toInt() == 2) {
                val identity = MessageIdentity.create(reader)
                val keyId = SkippedBase256.readLong(reader)
                val signId = SkippedBase256.readLong(reader)
                val sender = SkippedBase256.readLong(reader)

                val msgLength = SkippedBase256.readInt(reader)
                val msg = reader.read(msgLength)

                val signLength = SkippedBase256.readInt(reader)
                val sign = reader.read(signLength)

                AsymmetricCryptedMessage(
                    identity,
                    keyId,
                    signId,
                    msg,
                    sign,
                    sender
                )
            } else {
                null
            }
        }
    }

    private var keyId: Long
    private var signId: Long
    private var sender: Long = 0
    private var msg: ByteArray
    private var sign: ByteArray
    private var digest: DigestData

    @Auto
    constructor() {
        this.keyId = -1
        this.signId = -1
        this.msg = byteArrayOf()
        this.sign = byteArrayOf()
        this.digest = DigestData()
    }

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): ClientMessageContent {
        val msg = try {
            String(
                Crypto.asymmetricDecrypt(msg(), Kalmia.CLIENT.getPrivateKey(keyId(), true)),
                StandardCharsets.UTF_8
            )
        } catch (ex: Exception) {
            "The message is unable to decrypt, because: $ex"
        }

        val verified = try {
            Crypto.asymmetricVerify(
                msg(),
                sign(),
                Kalmia.CLIENT.getPublicKey(signId(), true)
            )
        } catch (ex: Exception) {
            false
        }

        try {
            return ClientMessageContent(
                sender(),
                "AsymmetricCryptedMessage{sender=${sender()}, keyId=${keyId()}, signId=${signId()}, msg=${msg}, sign=${
                    MessageDigger.digest(sign(), Sha3.SHA_512)
                }, digest36${digest().value36()}, verified=${verified}}",
                "(verified: $verified) $msg"
            )
        } catch (ex: Exception) {
            return ClientMessageContent(
                sender(),
                "AsymmetricCryptedMessage{sender=${sender()}, keyId=${keyId()}, signId=${signId()}, msg=${
                    MessageDigger.digest(msg(), Sha3.SHA_512)
                }, sign=${
                    MessageDigger.digest(sign(), Sha3.SHA_512)
                }, digest36=${digest().value36()}, verified=${verified}}",
                "(verified: $verified) $msg"
            )
        }
    }

    constructor(keyId: Long, signId: Long, msg: ByteArray, sign: ByteArray, sender: Long) {
        this.keyId = keyId
        this.signId = signId
        this.msg = msg
        this.sign = sign
        this.sender = sender
        this.digest = DigestData(
            Sha3.SHA_512,
            Mathematics.toBytes(
                MessageDigger.digest(
                    msg,
                    Sha3.SHA_512
                ),
                16
            )
        )
    }

    constructor(
        identity: MessageIdentity,
        keyId: Long,
        signId: Long,
        msg: ByteArray,
        sign: ByteArray,
        sender: Long
    ) : super(identity) {
        this.keyId = keyId
        this.signId = signId
        this.msg = msg
        this.sign = sign
        this.sender = sender
        this.digest = DigestData(
            Sha3.SHA_512,
            Mathematics.toBytes(
                MessageDigger.digest(
                    msg,
                    Sha3.SHA_512
                ),
                16
            )
        )
    }

    fun keyId(): Long = this.keyId

    fun signId(): Long = this.signId

    fun msg(): ByteArray = this.msg

    fun sign(): ByteArray = this.sign

    override fun sender(): Long = this.sender

    override fun digest(): DigestData = this.digest

    override fun toBytes(): ByteArray {
        return BytesUtil.concat(
            header(),
            identity().toBytes(),
            SkippedBase256.longToBuf(keyId()),
            SkippedBase256.longToBuf(signId()),
            SkippedBase256.longToBuf(sender()),
            SkippedBase256.intToBuf(msg().size),
            msg(),
            SkippedBase256.intToBuf(sign().size),
            sign()
        )
    }
}