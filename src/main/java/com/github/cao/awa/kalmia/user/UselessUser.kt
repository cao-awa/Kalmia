package com.github.cao.awa.kalmia.user

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.setting.Settings
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

class UselessUser : User {
    companion object {
        private val HEADER = byteArrayOf(-1)

        @JvmStatic
        fun create(reader: BytesReader): UselessUser? {
            return if (reader.read().toInt() == -1) {
                val identity = LongAndExtraIdentity.read(reader)
                UselessUser(identity)
            } else {
                null
            }
        }
    }

    constructor()
    constructor(identity: LongAndExtraIdentity) : super(identity)

    fun markTimestamp(): Long = identity().longValue()

    override fun toBytes(): ByteArray {
        return BytesUtil.concat(
            header(),
            identity().toBytes()
        )
    }

    override fun header(): ByteArray = HEADER

    override fun settings(): Settings = Settings()
}