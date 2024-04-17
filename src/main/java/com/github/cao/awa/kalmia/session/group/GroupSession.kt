package com.github.cao.awa.kalmia.session.group

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.session.Session
import com.github.cao.awa.viburnum.util.bytes.BytesUtil

import java.nio.charset.StandardCharsets

open class GroupSession(
    sessionIdentity: PureExtraIdentity, val displayName: String, var subscriberCount: Long
) : Session(sessionIdentity) {
    companion object {
        private val HEADER: ByteArray = byteArrayOf(3)

        @JvmStatic
        fun create(reader: BytesReader): GroupSession? {
            if (reader.read().toInt() == 3) {
                return GroupSession(
                    PureExtraIdentity.read(reader),
                    String(reader.read(Base256.readTag(reader)), StandardCharsets.UTF_8),
                    SkippedBase256.readLong(reader)
                )
            }
            return null
        }
    }

    override fun bytes(): ByteArray {
        return BytesUtil.concat(
            header(),
            identity().toBytes(),
            Base256.tagToBuf(this.displayName.length),
            displayName.toByteArray(StandardCharsets.UTF_8),
            SkippedBase256.longToBuf(this.subscriberCount)
        )
    }

    override fun accessible(accessIdentity: LongAndExtraIdentity): Boolean {
        return Kalmia.SERVER.sessionManager.accessible(identity(), accessIdentity).accessibleChat(true);
    }

    override fun header(): ByteArray {
        return HEADER
    }

    override fun displayName(): String {
        return displayName
    }

    fun subscriberCount(count: Long) {
        this.subscriberCount = count
    }
}
