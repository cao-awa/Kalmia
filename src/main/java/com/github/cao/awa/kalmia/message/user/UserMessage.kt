package com.github.cao.awa.kalmia.message.user

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.apricot.util.digger.MessageDigger.Sha3
import com.github.cao.awa.apricot.util.encryption.Crypto
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.ClientMessageContent
import com.github.cao.awa.kalmia.message.identity.MessageIdentity
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.nio.charset.StandardCharsets

class UserMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(2)

        @JvmStatic
        fun create(reader: BytesReader): UserMessage? {
            return if (reader.read().toInt() == 2) {
                val identity = MessageIdentity.create(reader)
                val keyId = SkippedBase256.readLong(reader)
                val signId = SkippedBase256.readLong(reader)
                val sender = SkippedBase256.readLong(reader)

                val msgLength = SkippedBase256.readInt(reader)
                val msg = reader.read(msgLength)

                val signLength = Base256.readTag(reader)
                val sign = reader.read(signLength)

                UserMessage(
                    identity,
                    keyId,
                    msg,
                    signId,
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

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): ClientMessageContent {
        var decrypted = byteArrayOf()

        val msg = try {
            if (keyId() == -1L) {
                String(
                    msg(),
                    StandardCharsets.UTF_8
                )
            } else {
                decrypted = Crypto.asymmetricDecrypt(msg(), Kalmia.CLIENT.getPrivateKey(keyId(), true))

                String(
                    decrypted,
                    StandardCharsets.UTF_8
                )
            }
        } catch (ex: Exception) {
            "The message is unable to decrypt, because: $ex"
        }

        val verified = if (signId() == -1L) false else try {
            Crypto.asymmetricVerify(
                decrypted,
                sign(),
                Kalmia.CLIENT.getPublicKey(signId(), true)
            )
        } catch (ex: Exception) {
            false
        }

        try {
            return ClientMessageContent(
                sender(),
                "UserMessage{sender=${sender()}, keyId=${keyId()}, signId=${signId()}, msg=${msg}, sign=${
                    MessageDigger.digest(sign(), Sha3.SHA_512)
                }, digest36${digest().value36()}, verified=${verified}}",
                msg
            )
        } catch (ex: Exception) {
            return ClientMessageContent(
                sender(),
                "UserMessage{sender=${sender()}, keyId=${keyId()}, signId=${signId()}, msg=${
                    MessageDigger.digest(msg(), Sha3.SHA_512)
                }, sign=${
                    MessageDigger.digest(sign(), Sha3.SHA_512)
                }, digest36=${digest().value36()}, verified=${verified}}",
                msg
            )
        }
    }

    constructor(keyId: Long, msg: ByteArray, signId: Long, sign: ByteArray, sender: Long) {
        this.keyId = keyId
        this.msg = msg
        this.signId = signId
        this.sign = sign
        this.sender = sender
        this.digest = DigestData.digest(
            Sha3.SHA_512,
            msg
        )
    }

    constructor(
        identity: MessageIdentity,
        keyId: Long,
        msg: ByteArray,
        signId: Long,
        sign: ByteArray,
        sender: Long
    ) : super(identity) {
        this.keyId = keyId
        this.msg = msg
        this.signId = signId
        this.sign = sign
        this.sender = sender
        this.digest = DigestData.digest(
            Sha3.SHA_512,
            msg
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
            Base256.tagToBuf(sign().size),
            sign()
        )
    }
}