package com.github.cao.awa.kalmia.session.database

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.annotations.number.encode.ShouldSkipped
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.key.BytesKey
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.session.Session
import com.github.cao.awa.kalmia.session.SessionAccessible
import com.github.cao.awa.kalmia.session.SessionAccessibleData
import com.github.cao.awa.kalmia.session.Sessions
import com.github.cao.awa.kalmia.setting.Settings
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.util.function.BiConsumer
import java.util.function.Consumer

class SessionDatabase(path: String) : KeyValueDatabase<BytesKey, Session?>(ApricotCollectionFactor::hashMap) {
    companion object {
        private val ROOT = byteArrayOf(1)
        private val ACCESSIBLE_DELIMITER = byteArrayOf(123)
        private val SETTINGS_DELIMITER = byteArrayOf(111)
        fun accessibleKey(
            sessionIdentity: PureExtraIdentity,
            accessIdentity: LongAndExtraIdentity
        ): BytesKey {
            return BytesKey(
                BytesUtil.concat(
                    sessionIdentity.extras(),
                    ACCESSIBLE_DELIMITER,
                    accessIdentity.toBytes()
                )
            )
        }

        fun settingsKey(@ShouldSkipped sessionIdentity: PureExtraIdentity): BytesKey {
            return BytesKey(
                BytesUtil.concat(
                    sessionIdentity.extras(),
                    SETTINGS_DELIMITER
                )
            )
        }
    }

    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    fun operation(action: BiConsumer<Long, Session?>) {
        val nextSeq = nextSeq()
        if (nextSeq > 0) {
            for (seq in 0 until nextSeq) {
                action.accept(
                    seq,
                    get(BytesKey(SkippedBase256.longToBuf(seq)))
                )
            }
        }
    }

    fun seq(): Long {
        val seqByte = this.delegate[BytesKey(ROOT)]
        return if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
    }

    fun nextSeq(): Long = seq() + 1

    override operator fun get(sessionIdentity: BytesKey): Session? {
        return cache()[
            sessionIdentity,
            { getSession(it) }
        ]
    }

    operator fun get(sessionIdentity: PureExtraIdentity): Session? {
        return this[BytesKey(sessionIdentity.extras())]
    }

    fun accessible(sessionIdentity: PureExtraIdentity, accessIdentity: LongAndExtraIdentity): SessionAccessibleData {
        val accessible = this.delegate[accessibleKey(
            sessionIdentity,
            accessIdentity
        )] ?: return SessionAccessibleData(SessionAccessible.DEFAULT_SETTINGS.clone())
        return SessionAccessibleData(accessible)
    }

    fun accessible(
        sessionIdentity: PureExtraIdentity,
        accessIdentity: LongAndExtraIdentity,
        data: SessionAccessibleData
    ) {
        this.delegate[accessibleKey(
            sessionIdentity,
            accessIdentity
        )] = data.bytes()
    }

    fun settings(identity: PureExtraIdentity): Settings {
        val settings = this.delegate[settingsKey(identity)] ?: return Settings()
        return Settings.create(BytesReader.of(settings))
    }

    fun settings(identity: PureExtraIdentity, settings: Settings) {
        this.delegate[settingsKey(identity)] = settings.toBytes()
    }

    fun banChat(identity: PureExtraIdentity, accessIdentity: LongAndExtraIdentity) {
        val key = accessibleKey(
            identity,
            accessIdentity
        )
        val accessible = SessionAccessible.banChat(this.delegate[key])
        this.delegate[key] = accessible
    }

    fun approveChat(identity: PureExtraIdentity, accessIdentity: LongAndExtraIdentity) {
        val key = accessibleKey(
            identity,
            accessIdentity
        )
        val accessible = SessionAccessible.approveChat(this.delegate[key])
        this.delegate[key] = accessible
    }

    private fun getSession(identity: PureExtraIdentity): Session {
        return getSession(BytesKey(identity.extras()))
    }

    private fun getSession(identity: BytesKey): Session {
        val bytes = this.delegate[identity] ?: return Sessions.INACCESSIBLE
        return Session.create(bytes)
    }

    override fun remove(@ShouldSkipped identity: BytesKey) {
        cache()
            .delete(
                identity
            ) {
                this.delegate.remove(it)
            }
    }

    fun remove(identity: PureExtraIdentity) {
        remove(BytesKey(identity.extras()))
    }

    fun remove(seq: Long) {
        remove(identity(seq))
    }

    fun identity(sid: Long): PureExtraIdentity {
        return PureExtraIdentity.create(this.delegate[BytesKey(SkippedBase256.longToBuf(sid))])
    }

    fun seqAll(action: Consumer<Long>) {
        val nextSeq = nextSeq()
        if (nextSeq > 0) {
            for (seq in 0 until nextSeq) {
                action.accept(seq)
            }
        }
    }

    fun deleteAll() {
        seqAll { remove(BytesKey(SkippedBase256.longToBuf(it))) }
    }

    fun identity(seq: BytesKey): PureExtraIdentity = PureExtraIdentity.create(this.delegate[seq])

    fun identity(seq: BytesKey, identity: PureExtraIdentity) {
        this.delegate[seq] = identity.toBytes()
    }

    fun add(session: Session): PureExtraIdentity {
        val nextSeq = nextSeq()
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)

        // Save the redirector to global id.
        identity(
            BytesKey(nextSeqByte),
            session.identity()
        )

        // Update index.
        curSeq(nextSeqByte)

        // Update message.
        set(
            session.identity(),
            session
        )
        return session.identity()
    }

    fun curSeq(curSeq: Long) = curSeq(SkippedBase256.longToBuf(curSeq))

    private fun curSeq(curSeq: ByteArray) {
        this.delegate[BytesKey(ROOT)] = curSeq
    }

    override fun set(sessionIdentity: BytesKey, session: Session?) {
        if (session == null) {
            remove(sessionIdentity)
            return
        }

        this.delegate[sessionIdentity] = session.bytes()
    }

    fun set(sessionIdentity: PureExtraIdentity, session: Session?) {
        set(
            BytesKey(sessionIdentity.extras()),
            session
        )
    }
}