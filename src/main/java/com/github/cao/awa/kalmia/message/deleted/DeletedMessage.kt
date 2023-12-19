package com.github.cao.awa.kalmia.message.deleted

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
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
                val identity = LongAndExtraIdentity.read(reader)
                val sender = LongAndExtraIdentity.read(reader)
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

    private val sender: LongAndExtraIdentity
    private val digestData: DigestData

    constructor(sender: LongAndExtraIdentity, digestData: DigestData) {
        this.sender = sender
        this.digestData = digestData
    }

    constructor(
        identity: LongAndExtraIdentity,
        sender: LongAndExtraIdentity,
        digestData: DigestData
    ) : super(identity) {
        this.sender = sender
        this.digestData = digestData
    }

    override fun sender(): LongAndExtraIdentity = this.sender

    override fun header(): ByteArray = HEADER

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
            sender().toBytes(),
            digest().serialize()
        )
    }

    override fun digest(): DigestData = this.digestData
}