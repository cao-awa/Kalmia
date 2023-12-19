package com.github.cao.awa.kalmia.message.user

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.apricot.util.digger.MessageDigger.Sha3
import com.github.cao.awa.apricot.util.encryption.Crypto
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.constant.KalmiaConstant
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.ClientMessageContent
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.nio.charset.StandardCharsets

class UserMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(2)

        @JvmStatic
        fun create(reader: BytesReader): UserMessage? {
            return if (reader.read().toInt() == 2) {
                val identity = LongAndExtraIdentity.read(reader)
                val keyIdentity = PureExtraIdentity.read(reader)
                val signId = PureExtraIdentity.read(reader)
                val sender = LongAndExtraIdentity.read(reader)

                val msgLength = SkippedBase256.readInt(reader)
                val msg = reader.read(msgLength)

                val signLength = Base256.readTag(reader)
                val sign = reader.read(signLength)

                UserMessage(
                    identity,
                    keyIdentity,
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

    private var keyIdentity: PureExtraIdentity
    private var signIdentity: PureExtraIdentity
    private var sender: LongAndExtraIdentity = KalmiaConstant.UNMARKED_LONG_AND_EXTRA_IDENTITY
    private var msg: ByteArray
    private var sign: ByteArray
    private var digest: DigestData

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): ClientMessageContent {
        var decrypted = byteArrayOf()

        val msg = try {
            if (keyIdentity() == KalmiaConstant.UNMARKED_PURE_IDENTITY) {
                String(
                    msg(),
                    StandardCharsets.UTF_8
                )
            } else {
                decrypted = Crypto.asymmetricDecrypt(msg(), Kalmia.CLIENT.getPrivateKey(keyIdentity(), true))

                String(
                    decrypted,
                    StandardCharsets.UTF_8
                )
            }
        } catch (ex: Exception) {
            "The message is unable to decrypt, because: $ex"
        }

        val verified = if (signIdentity() == KalmiaConstant.UNMARKED_PURE_IDENTITY) false else try {
            Crypto.asymmetricVerify(
                decrypted,
                sign(),
                Kalmia.CLIENT.getPublicKey(signIdentity(), true)
            )
        } catch (ex: Exception) {
            false
        }

        try {
            return ClientMessageContent(
                sender(),
                "UserMessage{sender=${sender()}, keyId=${keyIdentity()}, signId=${signIdentity()}, msg=${msg}, sign=${
                    MessageDigger.digest(sign(), Sha3.SHA_512)
                }, digest36${digest().value36()}, verified=${verified}}",
                msg
            )
        } catch (ex: Exception) {
            return ClientMessageContent(
                sender(),
                "UserMessage{sender=${sender()}, keyId=${keyIdentity()}, signId=${signIdentity()}, msg=${
                    MessageDigger.digest(msg(), Sha3.SHA_512)
                }, sign=${
                    MessageDigger.digest(sign(), Sha3.SHA_512)
                }, digest36=${digest().value36()}, verified=${verified}}",
                msg
            )
        }
    }

    constructor(
        keyIdentity: PureExtraIdentity,
        msg: ByteArray,
        signIdentity: PureExtraIdentity,
        sign: ByteArray,
        sender: LongAndExtraIdentity
    ) {
        this.keyIdentity = keyIdentity
        this.msg = msg
        this.signIdentity = signIdentity
        this.sign = sign
        this.sender = sender
        this.digest = DigestData.digest(
            Sha3.SHA_512,
            msg
        )
    }

    constructor(
        identity: LongAndExtraIdentity,
        keyIdentity: PureExtraIdentity,
        msg: ByteArray,
        signIdentity: PureExtraIdentity,
        sign: ByteArray,
        sender: LongAndExtraIdentity
    ) : super(identity) {
        this.keyIdentity = keyIdentity
        this.msg = msg
        this.signIdentity = signIdentity
        this.sign = sign
        this.sender = sender
        this.digest = DigestData.digest(
            Sha3.SHA_512,
            msg
        )
    }

    fun keyIdentity(): PureExtraIdentity = this.keyIdentity

    fun signIdentity(): PureExtraIdentity = this.signIdentity

    fun msg(): ByteArray = this.msg

    fun sign(): ByteArray = this.sign

    override fun sender(): LongAndExtraIdentity = this.sender

    override fun digest(): DigestData = this.digest

    override fun toBytes(): ByteArray {
        return BytesUtil.concat(
            header(),
            identity().toBytes(),
            keyIdentity().toBytes(),
            signIdentity().toBytes(),
            sender().toBytes(),
            SkippedBase256.intToBuf(msg().size),
            msg(),
            Base256.tagToBuf(sign().size),
            sign()
        )
    }
}