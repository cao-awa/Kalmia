package com.github.cao.awa.kalmia.message.identity

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.time.TimeUtil
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

class MessageIdentity(private val mills: Long, private val extras: ByteArray) {
    companion object {
        @JvmStatic
        fun create(reader: BytesReader): MessageIdentity {
            val length = Base256.readTag(reader)

            val dataReader = reader.reader(length)

            val mills = Base256.readLong(dataReader)
            val extras = dataReader.all()

            return MessageIdentity(
                mills,
                extras
            )
        }

        @JvmStatic
        fun create(extras: ByteArray): MessageIdentity {
            return MessageIdentity(
                TimeUtil.millions(),
                extras
            )
        }
    }

    fun mills(): Long = this.mills

    fun extras(): ByteArray = this.extras

    fun toBytes(): ByteArray {
        val mills = Base256.longToBuf(this.mills)
        val length = mills.size + this.extras.size
        return BytesUtil.concat(
            Base256.tagToBuf(length),
            mills,
            extras()
        )
    }
}