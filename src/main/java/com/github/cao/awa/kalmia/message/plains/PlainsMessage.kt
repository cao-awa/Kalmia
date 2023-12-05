package com.github.cao.awa.kalmia.message.plains

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.digest.DigestData
import com.github.cao.awa.kalmia.message.display.DisplayMessageContent
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.nio.charset.StandardCharsets

class PlainsMessage : Message {
    companion object {
        private val HEADER = byteArrayOf(1)

        @JvmStatic
        fun create(reader: BytesReader): PlainsMessage? {
            return if (reader.read().toInt() == 1) {
                val gid = reader.read(24)
                val sender = SkippedBase256.readLong(reader)
                val length = SkippedBase256.readInt(reader)
                val msg = String(
                    reader.read(length),
                    StandardCharsets.UTF_8
                )
                PlainsMessage(
                    gid,
                    msg,
                    sender
                )
            } else {
                null
            }
        }
    }

    private var sender: Long = 0
    private var msg: String
    private var digest: DigestData

    @Auto
    constructor() {
        this.msg = ""
        this.digest = DigestData()
    }

    override fun header(): ByteArray {
        return HEADER
    }

    override fun display(): DisplayMessageContent {
        return DisplayMessageContent(
            "PlainsMessage{sender=${sender()}, msg=${msg()}, digest=${digest().value36()}}",
            msg()
        )
    }

    constructor(msg: String, sender: Long) {
        this.msg = msg
        this.sender = sender
        this.digest = DigestData(
            MessageDigger.Sha3.SHA_512,
            Mathematics.toBytes(
                MessageDigger.digest(
                    msg,
                    MessageDigger.Sha3.SHA_512
                ),
                16
            )
        )
    }

    constructor(gid: ByteArray, msg: String, sender: Long) : super(gid) {
        this.msg = msg
        this.sender = sender
        this.digest = DigestData(
            MessageDigger.Sha3.SHA_512,
            Mathematics.toBytes(
                MessageDigger.digest(
                    msg,
                    MessageDigger.Sha3.SHA_512
                ),
                16
            )
        )
    }

    fun msg(): String {
        return this.msg
    }

    override fun sender(): Long {
        return this.sender
    }

    override fun digest(): DigestData {
        return this.digest
    }

    override fun toBytes(): ByteArray {
        return BytesUtil.concat(
            header(),
            globalId(),
            SkippedBase256.longToBuf(sender()),
            SkippedBase256.intToBuf(msg().length),
            msg().toByteArray(StandardCharsets.UTF_8)
        )
    }
}