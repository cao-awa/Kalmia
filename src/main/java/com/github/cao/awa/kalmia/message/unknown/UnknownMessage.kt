package com.github.cao.awa.kalmia.message.unknown

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.DisplayMessageContent
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

class UnknownMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(0)

        @JvmStatic
        fun create(reader: BytesReader): UnknownMessage? {
            return if (reader.read().toInt() == 0) {
                val gid = reader.read(24)
                val data = reader.all()
                UnknownMessage(
                    gid,
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
        this.digestData = DigestData(
            MessageDigger.Sha3.SHA_512,
            Mathematics.toBytes(
                MessageDigger.digest(
                    msgBytes,
                    MessageDigger.Sha3.SHA_512
                ),
                16
            )
        )
    }

    constructor(gid: ByteArray?, msgBytes: ByteArray) : super(gid) {
        this.msgBytes = msgBytes
        this.digestData = DigestData(
            MessageDigger.Sha3.SHA_512,
            Mathematics.toBytes(
                MessageDigger.digest(
                    msgBytes,
                    MessageDigger.Sha3.SHA_512
                ),
                16
            )
        )
    }

    override fun digest(): DigestData {
        return this.digestData
    }

    fun details(): ByteArray {
        return this.msgBytes
    }

    override fun sender(): Long {
        return -1
    }

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): DisplayMessageContent {
        return DisplayMessageContent(
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
            globalId(),
            details()
        )
    }
}