package com.github.cao.awa.kalmia.user.database

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.time.TimeUtil
import com.github.cao.awa.kalmia.annotations.number.encode.ShouldSkipped
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.user.DefaultUser
import com.github.cao.awa.kalmia.user.UselessUser
import com.github.cao.awa.kalmia.user.User
import com.github.cao.awa.kalmia.user.pubkey.PublicKeyIdentity
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.io.ByteArrayOutputStream
import java.security.PublicKey
import java.util.function.BiConsumer
import java.util.function.Consumer

class UserDatabase(path: String) : KeyValueDatabase<ByteArray, User>(ApricotCollectionFactor::hashMap) {
    companion object {
        private val ROOT = byteArrayOf(1)
        private val SESSION_DELIMITER = byteArrayOf(-127)
        private val SESSION_LISTENERS_DELIMITER = byteArrayOf(100)
        private val PUBLIC_KEY_DELIMITER = byteArrayOf(127)
    }

    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    fun publicKey(@ShouldSkipped uid: ByteArray): PublicKey? {
        val reader = BytesReader.of(
            this.delegate[BytesUtil.concat(
                uid,
                PUBLIC_KEY_DELIMITER
            )]
        )
        return if (reader.readable() > 1) {
            PublicKeyIdentity.createKey(
                Base256.readTag(reader),
                reader.all()
            )
        } else {
            null
        }
    }

    fun publicKey(@ShouldSkipped uid: ByteArray, publicKey: PublicKey) {
        this.delegate.put(
            BytesUtil.concat(
                uid,
                PUBLIC_KEY_DELIMITER
            ),
            PublicKeyIdentity.encodeKey(publicKey)
        )
    }

    fun operation(action: BiConsumer<Long, User>) {
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

    override operator fun get(@ShouldSkipped uid: ByteArray): User {
        return cache().get(
            uid
        ) {
            getUser(it)
        }
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
        val source = get(uid)
        put(
            uid,
            UselessUser(TimeUtil.nano())
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
        seqAll { seq: Long -> markUseless(SkippedBase256.longToBuf(seq!!)) }
    }

    fun add(user: User): Long {
        val nextSeq = nextSeq()
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)
        this.delegate.put(
            nextSeqByte,
            user.toBytes()
        )
        this.delegate.put(
            ROOT,
            nextSeqByte
        )
        return nextSeq
    }

    fun nextSeq(): Long {
        val seqByte = this.delegate[ROOT]
        val seq = if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
        return seq + 1
    }

    override fun put(@ShouldSkipped seq: ByteArray, user: User) {
        this.delegate.put(
            seq,
            user.toBytes()
        )
        if (user is DefaultUser) {
            publicKey(
                seq,
                user.publicKey()
            )
        }
    }

    fun session(@ShouldSkipped firstUserSeq: ByteArray, @ShouldSkipped targetUserSeq: ByteArray): ByteArray {
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
        this.delegate.put(
            sessionKey(
                firstUserSeq,
                targetUserSeq
            ),
            sessionId
        )
    }

    private fun sessionKey(@ShouldSkipped firstUserSeq: ByteArray, @ShouldSkipped targetUserSeq: ByteArray): ByteArray =
        BytesUtil.concat(
            firstUserSeq,
            SESSION_DELIMITER,
            targetUserSeq
        )

    fun sessionListeners(@ShouldSkipped seq: ByteArray): List<Long> {
        val reader = BytesReader.of(this.delegate.get(sessionListenersKey(seq)))

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

            this.delegate.put(
                sessionListenersKey(seq),
                output.toByteArray()
            )

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