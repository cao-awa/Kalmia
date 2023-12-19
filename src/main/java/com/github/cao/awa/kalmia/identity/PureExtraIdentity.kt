package com.github.cao.awa.kalmia.identity

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

open class PureExtraIdentity(private val extras: ByteArray) {
    companion object {
        @JvmStatic
        fun read(reader: BytesReader): PureExtraIdentity {
            val length = Base256.readTag(reader)

            return PureExtraIdentity(
                reader.read(length)
            )
        }

        @JvmStatic
        fun create(extras: ByteArray): PureExtraIdentity {
            return PureExtraIdentity(extras)
        }
    }

    fun extras(): ByteArray = this.extras

    fun toBytes(): ByteArray {
        return BytesUtil.concat(
            Base256.tagToBuf(this.extras.size),
            extras()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (this.javaClass != other.javaClass) return false

        other as PureExtraIdentity

        if (!this.extras.contentEquals(other.extras)) return false

        return true
    }

    override fun hashCode(): Int {
        return this.extras.contentHashCode()
    }

    override fun toString(): String = Mathematics.radix(toBytes(), 36)
}