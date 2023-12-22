package com.github.cao.awa.kalmia.user.database

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.key.BytesKey
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.user.UselessUser
import com.github.cao.awa.kalmia.user.User
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.io.ByteArrayOutputStream
import java.util.function.BiConsumer
import java.util.function.Consumer

class UserDatabase(path: String) : KeyValueDatabase<BytesKey, User?>(ApricotCollectionFactor::hashMap) {
    companion object {
        private val SEQ_REDIRECTOR = byteArrayOf(123)
        private val ROOT = byteArrayOf(1)
        private val SESSION_DELIMITER = byteArrayOf(-127)
        private val SESSION_LISTENERS_DELIMITER = byteArrayOf(100)
        private val KEY_STORE_DELIMITER = byteArrayOf(111)

        fun keyStoresKey(accessIdentity: LongAndExtraIdentity): BytesKey {
            return BytesKey(
                BytesUtil.concat(
                    accessIdentity.toBytes(),
                    KEY_STORE_DELIMITER
                )
            )
        }

        fun sessionListenersKey(accessIdentity: LongAndExtraIdentity): BytesKey =
            BytesKey(
                BytesUtil.concat(
                    accessIdentity.toBytes(),
                    SESSION_LISTENERS_DELIMITER
                )
            )

        fun sessionKey(selfUserIdentity: ByteArray, targetUserIdentity: ByteArray): BytesKey =
            BytesKey(
                BytesUtil.concat(
                    selfUserIdentity,
                    SESSION_DELIMITER,
                    targetUserIdentity
                )
            )
    }

    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    fun keyStores(accessIdentity: LongAndExtraIdentity): Set<PureExtraIdentity> {
        val key = keyStoresKey(accessIdentity)

        val reader = BytesReader.of(this.delegate[key])

        val result: HashSet<PureExtraIdentity> = ApricotCollectionFactor.hashSet()

        while (reader.readable(1)) {
            result.add(PureExtraIdentity.read(reader))
        }

        return result
    }

    fun keyStores(accessIdentity: LongAndExtraIdentity, stores: Set<PureExtraIdentity>) {
        try {
            val key = keyStoresKey(accessIdentity)

            val output = ByteArrayOutputStream();
            stores.forEach {
                output.writeBytes(it.toBytes())
            }

            this.delegate[key] = output.toByteArray()

            output.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun operation(action: BiConsumer<Long, User?>) {
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

    override operator fun get(uid: BytesKey): User? {
        return cache()[
            uid,
            { getUser(it) }
        ]
    }

    operator fun get(accessIdentity: LongAndExtraIdentity): User? {
        if (accessIdentity.extras().contentEquals(SEQ_REDIRECTOR)) {
            val identity = LongAndExtraIdentity.read(BytesReader.of(this.delegate[BytesKey(accessIdentity.toBytes())]))
            return this[identity]
        }
        return this[BytesKey(accessIdentity.toBytes())]
    }

    private fun getUser(accessIdentity: BytesKey): User? {
        val bytes = this.delegate[accessIdentity] ?: return null
        return User.create(bytes)
    }

    override fun remove(accessIdentity: BytesKey) {
        cache().delete(
            accessIdentity,
            this.delegate::remove
        )
    }

    fun markUseless(accessIdentity: BytesKey) {
        set(
            accessIdentity,
            UselessUser()
        )
    }

    fun markUseless(accessIdentity: LongAndExtraIdentity) {
        markUseless(BytesKey(accessIdentity.toBytes()))
    }

    fun seqAll(action: Consumer<Long>) {
        val nextSeq = nextSeq()
        if (nextSeq > 0) {
            for (seq in 0 until nextSeq) {
                action.accept(seq)
            }
        }
    }

    fun uselessAll() {
        seqAll { markUseless(BytesKey(SkippedBase256.longToBuf(it))) }
    }

    fun add(user: User): Long {
        val nextSeq = nextSeq()
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)

        // Write the user data.
        this[user.identity()] = user

        // Write the redirect using the seq.
        setSeqRedirect(
            nextSeq,
            user.identity()
        )

        this.delegate[BytesKey(ROOT)] = nextSeqByte
        return nextSeq
    }

    fun setSeqRedirect(seq: Long, identity: LongAndExtraIdentity) {
        val seqIdentity = LongAndExtraIdentity.create(seq, byteArrayOf(123))
        this.delegate[BytesKey(seqIdentity.toBytes())] = identity.toBytes()
    }

    fun seq(): Long {
        val seqByte = this.delegate[BytesKey(ROOT)]
        return if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
    }

    fun nextSeq(): Long = seq() + 1

    override operator fun set(accessIdentity: BytesKey, user: User?) {
        if (user == null) {
            markUseless(accessIdentity)
            return
        }

        this.delegate[accessIdentity] = user.toBytes()
    }

    operator fun set(accessIdentity: LongAndExtraIdentity, user: User?) {
        this[BytesKey(accessIdentity.toBytes())] = user
    }

    fun session(selfUserIdentity: LongAndExtraIdentity, targetUserIdentity: LongAndExtraIdentity): PureExtraIdentity? {
        val data = this.delegate[sessionKey(
            selfUserIdentity.toBytes(),
            targetUserIdentity.toBytes()
        )] ?: return null
        return PureExtraIdentity.create(data)
    }

    fun session(
        selfUserIdentity: LongAndExtraIdentity,
        targetUserIdentity: LongAndExtraIdentity,
        sessionId: PureExtraIdentity
    ) {
        this.delegate[sessionKey(
            selfUserIdentity.toBytes(),
            targetUserIdentity.toBytes()
        )] = sessionId.extras()
    }

    fun sessionListeners(accessIdentity: LongAndExtraIdentity): Set<PureExtraIdentity> {
        val reader = BytesReader.of(this.delegate[sessionListenersKey(accessIdentity)])

        val result: HashSet<PureExtraIdentity> = ApricotCollectionFactor.hashSet();

        while (reader.readable(1)) {
            result.add(PureExtraIdentity.read(reader))
        }

        return result
    }

    fun sessionListeners(accessIdentity: LongAndExtraIdentity, listeners: Set<PureExtraIdentity>) {
        val output = ByteArrayOutputStream();

        listeners.forEach {
            output.writeBytes(it.toBytes())
        }

        this.delegate[sessionListenersKey(accessIdentity)] = output.toByteArray()

        output.close()
    }
}