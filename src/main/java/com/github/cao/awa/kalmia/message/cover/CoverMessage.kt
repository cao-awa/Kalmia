package com.github.cao.awa.kalmia.message.cover

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.ClientMessageContent
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.nio.charset.StandardCharsets

class CoverMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(5)

        @JvmStatic
        fun create(reader: BytesReader): CoverMessage? {
            return if (reader.read().toInt() == 5) {
                val identity = LongAndExtraIdentity.read(reader)

                val sourceMessageLength = SkippedBase256.readInt(reader)
                val sourceMessage = reader.read(sourceMessageLength)
                val sourceSender = LongAndExtraIdentity.read(reader)
                val sourceSignIdentity = PureExtraIdentity.read(reader)
                val sourceSignLength = Base256.readTag(reader)
                val sourceSign = reader.read(sourceSignLength)

                val coverMessageLength = SkippedBase256.readInt(reader)
                val coverMessage = reader.read(coverMessageLength)
                val coverSender = LongAndExtraIdentity.read(reader)
                val coverSignIdentity = PureExtraIdentity.read(reader)
                val coverSignLength = Base256.readTag(reader)
                val coverSign = reader.read(coverSignLength)

                CoverMessage(
                    identity,
                    sourceMessage,
                    sourceSender,
                    sourceSignIdentity,
                    sourceSign,
                    coverMessage,
                    coverSender,
                    coverSignIdentity,
                    coverSign
                )
            } else {
                null
            }
        }
    }

    private val sourceMessage: ByteArray
    private val sourceSender: LongAndExtraIdentity
    private val sourceSignIdentity: PureExtraIdentity
    private val sourceSign: ByteArray
    private val sourceDigest: DigestData
    private val coverMessage: ByteArray
    private val coverSender: LongAndExtraIdentity
    private val coverSignIdentity: PureExtraIdentity
    private val coverSign: ByteArray
    private val coverDigest: DigestData

    constructor(
        sourceMessage: ByteArray,
        sourceSender: LongAndExtraIdentity,
        sourceSignIdentity: PureExtraIdentity,
        sourceSign: ByteArray,
        coverMessage: ByteArray,
        coverSender: LongAndExtraIdentity,
        coverSignIdentity: PureExtraIdentity,
        coverSign: ByteArray
    ) {
        this.sourceMessage = sourceMessage
        this.sourceSender = sourceSender
        this.sourceSignIdentity = sourceSignIdentity
        this.sourceSign = sourceSign
        this.sourceDigest = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            sourceMessage
        )
        this.coverMessage = coverMessage
        this.coverSender = coverSender
        this.coverSignIdentity = coverSignIdentity
        this.coverSign = coverSign
        this.coverDigest = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            coverMessage
        )
    }

    constructor(
        identity: LongAndExtraIdentity,
        sourceMessage: ByteArray,
        sourceSender: LongAndExtraIdentity,
        sourceSignIdentity: PureExtraIdentity,
        sourceSign: ByteArray,
        coverMessage: ByteArray,
        coverSender: LongAndExtraIdentity,
        coverSignIdentity: PureExtraIdentity,
        coverSign: ByteArray
    ) : super(identity) {
        this.sourceMessage = sourceMessage
        this.sourceSender = sourceSender
        this.sourceSignIdentity = sourceSignIdentity
        this.sourceSign = sourceSign
        this.sourceDigest = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            sourceMessage
        )
        this.coverMessage = coverMessage
        this.coverSender = coverSender
        this.coverSignIdentity = coverSignIdentity
        this.coverSign = coverSign
        this.coverDigest = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            coverMessage
        )
    }

    override fun sender(): LongAndExtraIdentity = coverSender()

    override fun digest(): DigestData = coverDigest()

    fun sourceMessage(): ByteArray = this.sourceMessage

    fun sourceSender(): LongAndExtraIdentity = this.sourceSender

    fun sourceSignIdentity(): PureExtraIdentity = this.sourceSignIdentity

    fun sourceSign(): ByteArray = this.sourceSign

    fun sourceDigest(): DigestData = this.sourceDigest

    fun coverMessage(): ByteArray = this.coverMessage

    fun coverSender(): LongAndExtraIdentity = this.coverSender

    fun coverSignIdentity(): PureExtraIdentity = this.coverSignIdentity

    fun coverSign(): ByteArray = this.coverSign

    fun coverDigest(): DigestData = this.coverDigest

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): ClientMessageContent {
        return ClientMessageContent(
            sourceSender(),
            String(
                sourceMessage(),
                StandardCharsets.UTF_8
            ),
            String(
                coverMessage(),
                StandardCharsets.UTF_8
            )
        )
    }

    override fun toBytes(): ByteArray {
        return BytesUtil.concat(
            header(),
            identity().toBytes(),
            SkippedBase256.intToBuf(sourceMessage().size),
            sourceMessage(),
            sourceSender().toBytes(),
            sourceSignIdentity().toBytes(),
            Base256.tagToBuf(sourceSign().size),
            sourceSign(),
            SkippedBase256.intToBuf(coverMessage().size),
            coverMessage(),
            coverSender().toBytes(),
            coverSignIdentity().toBytes(),
            Base256.tagToBuf(coverSign().size),
            coverSign()
        )
    }
}