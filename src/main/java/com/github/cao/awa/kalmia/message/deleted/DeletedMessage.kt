package com.github.cao.awa.kalmia.message.deleted

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.DisplayMessageContent
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

class DeletedMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(-1)

        @JvmStatic
        fun create(reader: BytesReader): DeletedMessage? {
            return if (reader.read().toInt() == -1) {
                val gid = reader.read(24)
                val sender = SkippedBase256.readLong(reader)
                val digestData = DigestData.create(reader)
                DeletedMessage(
                    gid,
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

    constructor(globalId: ByteArray, sender: Long, digestData: DigestData) : super(globalId) {
        this.sender = sender
        this.digestData = digestData
    }

    override fun sender(): Long {
        return this.sender
    }

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): DisplayMessageContent {
        return DisplayMessageContent(
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
            globalId(),
            SkippedBase256.longToBuf(sender()),
            digest().serialize()
        )
    }

    override fun digest(): DigestData {
        return this.digestData
    }
}