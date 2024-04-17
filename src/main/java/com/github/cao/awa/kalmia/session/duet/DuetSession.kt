package com.github.cao.awa.kalmia.session.duet

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.session.Session
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

class DuetSession(
    sessionIdentity: PureExtraIdentity, val target1: LongAndExtraIdentity, val target2: LongAndExtraIdentity
) : Session(sessionIdentity) {
    companion object {
        private val HEADER: ByteArray = byteArrayOf(0)

        @JvmStatic
        fun create(reader: BytesReader): DuetSession? {
            if (reader.read().toInt() == 1) {
                return DuetSession(
                    PureExtraIdentity.read(reader), LongAndExtraIdentity.read(reader), LongAndExtraIdentity.read(reader)
                )
            }
            return null
        }
    }


    override fun bytes(): ByteArray {
        return BytesUtil.concat(header(), identity().toBytes(), this.target1.toBytes(), this.target2.toBytes())
    }

    override fun accessible(accessIdentity: LongAndExtraIdentity): Boolean {
        return Kalmia.SERVER.sessionManager.accessible(identity(), accessIdentity).accessibleChat(true)
    }

    override fun header(): ByteArray {
        return HEADER
    }

    override fun displayName(): String {
        return target1.toString()
    }

    fun opposite(userId: LongAndExtraIdentity): LongAndExtraIdentity {
        return when (userId) {
            target1 -> target2
            target2 -> target1
            else -> userId
        }
    }
}
