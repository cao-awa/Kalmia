package com.github.cao.awa.kalmia.message.deleted

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.identity.MillsAndExtraIdentity
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.ClientMessageContent
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

class DeletedMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(-1)

        @JvmStatic
        fun create(reader: BytesReader): DeletedMessage? {
            return if (reader.read().toInt() == -1) {
                val identity = MillsAndExtraIdentity.create(reader)
                val sender = SkippedBase256.readLong(reader)
                val digestData = DigestData.create(reader)
                DeletedMessage(
                    identity,
                    sender,
                    digestData
                )
            } else {
                null
            }
        }
    }

    private val sender: Long
    private val digestData: DigestData

    constructor(sender: Long, digestData: DigestData) {
        this.sender = sender
        this.digestData = digestData
    }

    constructor(identity: MillsAndExtraIdentity, sender: Long, digestData: DigestData) : super(identity) {
        this.sender = sender
        this.digestData = digestData
    }

    override fun sender(): Long {
        return this.sender
    }

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): ClientMessageContent {
        return ClientMessageContent(
            sender(),
            "DeletedMessage{sender=${sender()}, digest=${digest().value36()}}",
            """
                        This message has been deleted
                        sender is: ${sender()}
                      digest is: ${digest().value36()}"""
                .trimIndent()
        )
    }

    override fun toBytes(): ByteArray {
        return BytesUtil.concat(
            header(),
            identity().toBytes(),
            SkippedBase256.longToBuf(sender()),
            digest().serialize()
        )
    }

    override fun digest(): DigestData {
        return this.digestData
    }
}