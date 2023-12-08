package com.github.cao.awa.kalmia.user.database

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.time.TimeUtil
import com.github.cao.awa.kalmia.annotations.number.encode.ShouldSkipped
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
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
    }

    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    fun keyStores(uid: ByteArray): List<Long> {
        val key = BytesUtil.concat(
            uid,
            KEY_STORE_DELIMITER
        )

        val reader = BytesReader.of(this.delegate[key])

        val result: ArrayList<Long> = ApricotCollectionFactor.arrayList();

        while (reader.readable(1)) {
            result.add(SkippedBase256.readLong(reader))
        }

        return result
    }

    fun keyStores(uid: ByteArray, stores: List<Long>) {
        try {
            val key = BytesUtil.concat(
                uid,
                KEY_STORE_DELIMITER
            )

            val output = ByteArrayOutputStream();
            stores.forEach {
                output.writeBytes(SkippedBase256.longToBuf(it))
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

    override operator fun get(@ShouldSkipped uid: ByteArray): User? {
        return cache()[
            uid,
            { getUser(it) }
        ]
    }

    private fun getUser(uid: ByteArray): User? {
        val bytes = this.delegate[uid]
        return if (bytes == null || bytes.isEmpty()) {
            null
        } else User.create(bytes)
    }

    override fun remove(uid: ByteArray) {
        cache().delete(
            uid
        ) {
            this.delegate.remove(it)
        }
    }

    fun markUseless(@ShouldSkipped uid: ByteArray) {
        set(
            uid,
            UselessUser(TimeUtil.millions())
        )
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

    override fun set(@ShouldSkipped seq: ByteArray, user: User?) {
        if (user == null) {
            markUseless(seq)
            return
        }

        this.delegate[seq] = user.toBytes()
    }

    fun session(@ShouldSkipped firstUserSeq: ByteArray, @ShouldSkipped targetUserSeq: ByteArray): ByteArray? {
        return this.delegate[sessionKey(
            firstUserSeq,
            targetUserSeq
        )]
    }

    fun session(
        @ShouldSkipped firstUserSeq: ByteArray,
        @ShouldSkipped targetUserSeq: ByteArray,
        @ShouldSkipped sessionId: ByteArray
    ) {
        this.delegate[sessionKey(
            firstUserSeq,
            targetUserSeq
        )] = sessionId
    }

    private fun sessionKey(@ShouldSkipped firstUserSeq: ByteArray, @ShouldSkipped targetUserSeq: ByteArray): ByteArray =
        BytesUtil.concat(
            firstUserSeq,
            SESSION_DELIMITER,
            targetUserSeq
        )

    fun sessionListeners(@ShouldSkipped seq: ByteArray): List<Long> {
        val reader = BytesReader.of(this.delegate[sessionListenersKey(seq)])

        val result: ArrayList<Long> = ApricotCollectionFactor.arrayList();

        while (reader.readable(1)) {
            result.add(SkippedBase256.readLong(reader))
        }

        return result
    }

    fun sessionListeners(@ShouldSkipped seq: ByteArray, listeners: List<Long>) {
        try {
            val output = ByteArrayOutputStream();
            listeners.forEach {
                output.writeBytes(SkippedBase256.longToBuf(it))
            }

            this.delegate[sessionListenersKey(seq)] = output.toByteArray()

            output.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun sessionListenersKey(@ShouldSkipped seq: ByteArray): ByteArray =
        BytesUtil.concat(
            seq,
            SESSION_LISTENERS_DELIMITER
        )
}