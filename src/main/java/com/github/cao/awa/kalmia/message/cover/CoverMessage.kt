package com.github.cao.awa.kalmia.message.cover

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.ClientMessageContent
import com.github.cao.awa.kalmia.message.identity.MessageIdentity
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.nio.charset.StandardCharsets

class CoverMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(5)

        @JvmStatic
        fun create(reader: BytesReader): CoverMessage? {
            return if (reader.read().toInt() == 5) {
                val identity = MessageIdentity.create(reader)

                val sourceMessageLength = SkippedBase256.readInt(reader)
                val sourceMessage = reader.read(sourceMessageLength)
                val sourceSender = SkippedBase256.readLong(reader)
                val sourceSignId = SkippedBase256.readLong(reader)
                val sourceSignLength = Base256.readTag(reader)
                val sourceSign = reader.read(sourceSignLength)

                val coverMessageLength = SkippedBase256.readInt(reader)
                val coverMessage = reader.read(coverMessageLength)
                val coverSender = SkippedBase256.readLong(reader)
                val coverSignId = SkippedBase256.readLong(reader)
                val coverSignLength = Base256.readTag(reader)
                val coverSign = reader.read(coverSignLength)

                CoverMessage(
                    identity,
                    sourceMessage,
                    sourceSender,
                    sourceSignId,
                    sourceSign,
                    coverMessage,
                    coverSender,
                    coverSignId,
                    coverSign
                )
            } else {
                null
            }
        }
    }

    private val sourceMessage: ByteArray
    private val sourceSender: Long
    private val sourceSignId: Long
    private val sourceSign: ByteArray
    private val sourceDigest: DigestData
    private val coverMessage: ByteArray
    private val coverSender: Long
    private val coverSignId: Long
    private val coverSign: ByteArray
    private val coverDigest: DigestData

    constructor(
        sourceMessage: ByteArray,
        sourceSender: Long,
        sourceSignId: Long,
        sourceSign: ByteArray,
        coverMessage: ByteArray,
        coverSender: Long,
        coverSignId: Long,
        coverSign: ByteArray
    ) {
        this.sourceMessage = sourceMessage
        this.sourceSender = sourceSender
        this.sourceSignId = sourceSignId
        this.sourceSign = sourceSign
        this.sourceDigest = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            sourceMessage
        )
        this.coverMessage = coverMessage
        this.coverSender = coverSender
        this.coverSignId = coverSignId
        this.coverSign = coverSign
        this.coverDigest = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            coverMessage
        )
    }

    constructor(
        identity: MessageIdentity,
        sourceMessage: ByteArray,
        sourceSender: Long,
        sourceSignId: Long,
        sourceSign: ByteArray,
        coverMessage: ByteArray,
        coverSender: Long,
        coverSignId: Long,
        coverSign: ByteArray
    ) : super(identity) {
        this.sourceMessage = sourceMessage
        this.sourceSender = sourceSender
        this.sourceSignId = sourceSignId
        this.sourceSign = sourceSign
        this.sourceDigest = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            sourceMessage
        )
        this.coverMessage = coverMessage
        this.coverSender = coverSender
        this.coverSignId = coverSignId
        this.coverSign = coverSign
        this.coverDigest = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            coverMessage
        )
    }

    override fun sender(): Long = coverSender()

    override fun digest(): DigestData = coverDigest()

    fun sourceMessage(): ByteArray = this.sourceMessage

    fun sourceSender(): Long = this.sourceSender

    fun sourceSignId(): Long = this.sourceSignId

    fun sourceSign(): ByteArray = this.sourceSign

    fun sourceDigest(): DigestData = this.sourceDigest

    fun coverMessage(): ByteArray = this.coverMessage

    fun coverSender(): Long = this.coverSender

    fun coverSignId(): Long = this.coverSignId

    fun coverSign(): ByteArray = this.coverSign

    fun coverDigest(): DigestData = this.coverDigest

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): ClientMessageContent {
        return ClientMessageContent(
            sender(),
            "CoverMessage",
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
            SkippedBase256.longToBuf(sourceSender()),
            SkippedBase256.longToBuf(sourceSignId()),
            Base256.tagToBuf(sourceSign().size),
            sourceSign(),
            SkippedBase256.intToBuf(coverMessage().size),
            coverMessage(),
            SkippedBase256.longToBuf(coverSender()),
            SkippedBase256.longToBuf(coverSignId()),
            Base256.tagToBuf(coverSign().size),
            coverSign()
        )
    }
}