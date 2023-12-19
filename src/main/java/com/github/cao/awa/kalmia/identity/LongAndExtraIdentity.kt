package com.github.cao.awa.kalmia.identity

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

open class LongAndExtraIdentity(private val longValue: Long, private val extras: ByteArray) {
    companion object {
        @JvmStatic
        fun read(reader: BytesReader): LongAndExtraIdentity {
            val length = Base256.readTag(reader)

            val dataReader = reader.reader(length)

            val mills = Base256.readLong(dataReader)
            val extras = dataReader.all()

            return LongAndExtraIdentity(
                mills,
                extras
            )
        }

        @JvmStatic
        fun create(longValue: Long, extras: ByteArray): LongAndExtraIdentity {
            return LongAndExtraIdentity(
                longValue,
                extras
            )
        }
    }

    fun longValue(): Long = this.longValue

    fun extras(): ByteArray = this.extras

    fun toBytes(): ByteArray {
        val mills = Base256.longToBuf(this.longValue)
        val length = mills.size + this.extras.size
        return BytesUtil.concat(
            Base256.tagToBuf(length),
            mills,
            extras()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (this.javaClass != other.javaClass) return false

        other as LongAndExtraIdentity

        if (this.longValue != other.longValue) return false
        if (!this.extras.contentEquals(other.extras)) return false

        return true
    }

    override fun hashCode(): Int {
        val millsHash = this.longValue.hashCode()
        return 31 * millsHash + this.extras.contentHashCode()
    }

    override fun toString(): String = Mathematics.radix(toBytes(), 36)
}