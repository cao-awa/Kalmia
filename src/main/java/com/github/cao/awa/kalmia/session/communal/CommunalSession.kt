package com.github.cao.awa.kalmia.session.communal

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.session.group.GroupSession

import java.nio.charset.StandardCharsets

class CommunalSession(sessionIdentity: PureExtraIdentity, displayName: String, subscriberCount: Long) :
    GroupSession(sessionIdentity, displayName, subscriberCount) {
    companion object {
        @JvmField
        val TEST_COMMUNAL_IDENTITY: PureExtraIdentity =
            PureExtraIdentity.create(byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16))
        val TEST_COMMUNAL: CommunalSession = CommunalSession(TEST_COMMUNAL_IDENTITY, "Test public session", 0);
        private val HEADER: ByteArray = byteArrayOf(2)

        @JvmStatic
        fun create(reader: BytesReader): CommunalSession? {
            if (reader.read().toInt() == 2) {
                return CommunalSession(
                    PureExtraIdentity.read(reader),
                    String(reader.read(Base256.readTag(reader)), StandardCharsets.UTF_8),
                    SkippedBase256.readLong(reader)
                )
            }
            return null
        }
    }

    override fun accessible(accessIdentity: LongAndExtraIdentity): Boolean {
        return Kalmia.SERVER.sessionManager.accessible(identity(), accessIdentity).accessibleChat(false)
    }

    override fun header(): ByteArray {
        return HEADER
    }
}
