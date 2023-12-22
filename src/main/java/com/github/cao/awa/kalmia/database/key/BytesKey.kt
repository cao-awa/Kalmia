package com.github.cao.awa.kalmia.database.key

class BytesKey(private val key: ByteArray) {
    companion object {
        @JvmStatic
        fun of(key: ByteArray): BytesKey = BytesKey(key)
    }

    fun key(): ByteArray = this.key

    override fun equals(o: Any?): Boolean = this === o || o is BytesKey && this.key.contentEquals(o.key)

    override fun hashCode(): Int = this.key.contentHashCode()
}
