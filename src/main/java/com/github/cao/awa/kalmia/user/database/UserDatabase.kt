package com.github.cao.awa.kalmia.user.database

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
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

class UserDatabase(path: String) : KeyValueDatabase<ByteArray, User?>(ApricotCollectionFactor::hashMap) {
    companion object {
        private val ROOT = byteArrayOf(1)
        private val SESSION_DELIMITER = byteArrayOf(-127)
        private val SESSION_LISTENERS_DELIMITER = byteArrayOf(100)
        private val KEY_STORE_DELIMITER = byteArrayOf(111)

        fun keyStoresKey(accessIdentity: LongAndExtraIdentity): ByteArray {
            return BytesUtil.concat(
                accessIdentity.toBytes(),
                KEY_STORE_DELIMITER
            )
        }
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
                    get(SkippedBase256.longToBuf(seq))
                )
            }
        }
    }

    override operator fun get(uid: ByteArray): User? {
        return cache()[
            uid,
            { getUser(it) }
        ]
    }

    operator fun get(accessIdentity: LongAndExtraIdentity): User? {
        return this[accessIdentity.toBytes()]
    }

    private fun getUser(accessIdentity: ByteArray): User? {
        val bytes = this.delegate[accessIdentity] ?: return null
        return User.create(bytes)
    }

    override fun remove(accessIdentity: ByteArray) {
        cache().delete(
            accessIdentity,
            this.delegate::remove
        )
    }

    fun markUseless(accessIdentity: ByteArray) {
        set(
            accessIdentity,
            UselessUser()
        )
    }

    fun markUseless(accessIdentity: LongAndExtraIdentity) {
        markUseless(accessIdentity.toBytes())
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
        seqAll { markUseless(SkippedBase256.longToBuf(it)) }
    }

    fun add(user: User): Long {
        val nextSeq = nextSeq()
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)
        this[nextSeqByte] = user
        this.delegate[ROOT] = nextSeqByte
        return nextSeq
    }

    fun seq(): Long {
        val seqByte = this.delegate[ROOT]
        return if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
    }

    fun nextSeq(): Long = seq() + 1

    override operator fun set(accessIdentity: ByteArray, user: User?) {
        if (user == null) {
            markUseless(accessIdentity)
            return
        }

        this.delegate[accessIdentity] = user.toBytes()
    }

    operator fun set(accessIdentity: LongAndExtraIdentity, user: User?) {
        this[accessIdentity.toBytes()] = user
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

    private fun sessionKey(selfUserIdentity: ByteArray, targetUserIdentity: ByteArray): ByteArray =
        BytesUtil.concat(
            selfUserIdentity,
            SESSION_DELIMITER,
            targetUserIdentity
        )

    fun sessionListeners(accessIdentity: LongAndExtraIdentity): List<PureExtraIdentity> {
        val reader = BytesReader.of(this.delegate[sessionListenersKey(accessIdentity)])

        val result: ArrayList<PureExtraIdentity> = ApricotCollectionFactor.arrayList();

        while (reader.readable(1)) {
            result.add(PureExtraIdentity.read(reader))
        }

        return result
    }

    fun sessionListeners(accessIdentity: LongAndExtraIdentity, listeners: List<PureExtraIdentity>) {
        val output = ByteArrayOutputStream();

        listeners.forEach {
            output.writeBytes(it.toBytes())
        }

        this.delegate[sessionListenersKey(accessIdentity)] = output.toByteArray()

        output.close()
    }

    private fun sessionListenersKey(accessIdentity: LongAndExtraIdentity): ByteArray =
        BytesUtil.concat(
            accessIdentity.toBytes(),
            SESSION_LISTENERS_DELIMITER
        )
}