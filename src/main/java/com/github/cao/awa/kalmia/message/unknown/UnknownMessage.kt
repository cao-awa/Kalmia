package com.github.cao.awa.kalmia.message.unknown

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.ClientMessageContent
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

class UnknownMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(0)

        @JvmStatic
        fun create(reader: BytesReader): UnknownMessage? {
            return if (reader.read().toInt() == 0) {
                val identity = LongAndExtraIdentity.read(reader)
                val data = reader.all()
                UnknownMessage(
                    identity,
                    data
                )
            } else {
                null
            }
        }
    }

    @AutoData
    private var msgBytes: ByteArray

    @AutoData
    private var digestData: DigestData

    @Auto
    constructor() {
        this.msgBytes = BytesUtil.EMPTY
        this.digestData = DigestData()
    }

    constructor(msgBytes: ByteArray) {
        this.msgBytes = msgBytes
        this.digestData = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            msgBytes
        )
    }

    constructor(identity: LongAndExtraIdentity, msgBytes: ByteArray) : super(identity) {
        this.msgBytes = msgBytes
        this.digestData = DigestData.digest(
            MessageDigger.Sha3.SHA_512,
            msgBytes
        )
    }

    override fun digest(): DigestData {
        return this.digestData
    }

    fun details(): ByteArray {
        return this.msgBytes
    }

    override fun sender(): LongAndExtraIdentity {
        return LongAndExtraIdentity.create(-1, BytesRandomIdentifier.create(16))
    }

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): ClientMessageContent {
        return ClientMessageContent(
            sender(),
            "PlainsMessage{sender=${sender()}, msg=${Mathematics.radix(details(), 36)}, digest=${digest().value36()}}",
            """
                This message unable to display, it digest is:
                ${digest().value36()}
                sender of this message is:
                ${sender()}
                it has theres data: 
                ${Mathematics.radix(details(), 36)}
                """
        )
    }

    override fun toBytes(): ByteArray {
        return BytesUtil.concat(
            header(),
            identity().toBytes(),
            details()
        )
    }
}