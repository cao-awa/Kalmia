package com.github.cao.awa.kalmia.session.database

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.annotations.number.encode.ShouldSkipped
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.session.Session
import com.github.cao.awa.kalmia.session.SessionAccessible
import com.github.cao.awa.kalmia.session.SessionAccessibleData
import com.github.cao.awa.kalmia.session.Sessions
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.util.function.BiConsumer
import java.util.function.Consumer

class SessionDatabase(path: String) : KeyValueDatabase<ByteArray, Session>(ApricotCollectionFactor::hashMap) {
    companion object {
        private val ROOT = byteArrayOf(1)
        private val ACCESSIBLE_DELIMITER = byteArrayOf(123)
        fun accessibleKey(@ShouldSkipped sid: ByteArray, @ShouldSkipped uid: ByteArray): ByteArray {
            return BytesUtil.concat(
                sid,
                ACCESSIBLE_DELIMITER,
                uid
            )
        }
    }

    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    fun operation(action: BiConsumer<Long, Session>) {
        val nextSeq = nextSeq()
        if (nextSeq > 0) {
            for (seq in 0 until nextSeq) {
                action.accept(
                    seq,
                    get(SkippedBase256.longToBuf(seq))
                )
            }
        }
    }

    fun nextSeq(): Long {
        val seqByte = this.delegate[ROOT]
        val seq = if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
        return seq + 1
    }

    override operator fun get(@ShouldSkipped sid: ByteArray): Session {
        return cache().get(
            sid
        ) {
            getSession(it)
        }
    }

    fun accessible(@ShouldSkipped sid: ByteArray, @ShouldSkipped uid: ByteArray): SessionAccessibleData {
        val accessible = this.delegate[accessibleKey(
            sid,
            uid
        )] ?: return SessionAccessibleData(SessionAccessible.DEFAULT_SETTINGS.clone())
        return SessionAccessibleData(accessible)
    }

    fun accessible(@ShouldSkipped sid: ByteArray, @ShouldSkipped uid: ByteArray, data: SessionAccessibleData) {
        this.delegate.put(
            accessibleKey(
                sid,
                uid
            ),
            data.bytes()
        )
    }

    fun banChat(@ShouldSkipped sid: ByteArray, @ShouldSkipped uid: ByteArray) {
        val key = accessibleKey(
            sid,
            uid
        )
        val accessible = SessionAccessible.banChat(this.delegate[key])
        this.delegate.put(
            key,
            accessible
        )
    }

    fun approveChat(@ShouldSkipped sid: ByteArray, @ShouldSkipped uid: ByteArray) {
        val key = accessibleKey(
            sid,
            uid
        )
        val accessible = SessionAccessible.approveChat(this.delegate[key])
        this.delegate.put(
            key,
            accessible
        )
    }

    private fun getSession(@ShouldSkipped sid: ByteArray): Session {
        val bytes = this.delegate[sid]
        return if (bytes == null || bytes.isEmpty()) Sessions.INACCESSIBLE else Session.create(bytes)
    }

    override fun remove(@ShouldSkipped sid: ByteArray) {
        cache()
            .delete(
                sid
            ) {
                this.delegate.remove(it)
            }
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
        seqAll { seq: Long -> remove(SkippedBase256.longToBuf(seq!!)) }
    }

    fun add(session: Session): Long {
        val nextSeq = nextSeq()
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)
        put(
            nextSeqByte,
            session
        )
        this.delegate.put(
            ROOT,
            nextSeqByte
        )
        return nextSeq
    }

    override fun put(@ShouldSkipped seq: ByteArray, session: Session) {
        this.delegate.put(
            seq,
            session.bytes()
        )
    }
}