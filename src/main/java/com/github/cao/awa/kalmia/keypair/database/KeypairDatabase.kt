package com.github.cao.awa.kalmia.keypair.database

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity
import com.github.cao.awa.kalmia.keypair.exception.NotEncryptedException
import com.github.cao.awa.kalmia.keypair.pair.EmptyKeyPair
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import java.util.function.BiConsumer
import java.util.function.Consumer

class KeypairDatabase(path: String?) : KeyValueDatabase<ByteArray, KeyPair>(ApricotCollectionFactor::hashMap) {
    companion object {
        private val ROOT = byteArrayOf(1)
    }

    private val delegate: KeyValueBytesDatabase

    init {
        delegate = DatabaseProviders.bytes(path)
    }

    override fun put(seq: ByteArray, keypair: KeyPair) {
        if (keypair.private != null) {
            throw NotEncryptedException("The private key required encrypt")
        }
    }

    fun putPrivate(seq: ByteArray, privateKey: KeyStore<out PrivateKey>) {
        privateKey.decode(true)

        val store = createStore(seq)
        val putStore = KeyStoreIdentity.createKeyPairStore(
            store.publicKey().decode(),
            privateKey.key()
        )

        this.delegate.put(
            seq,
            putStore.toBytes()
        )
    }

    fun putPublic(seq: ByteArray, publicKey: KeyStore<out PublicKey>) {
        publicKey.decode()

        val store = createStore(seq)
        val putStore = KeyStoreIdentity.createKeyPairStore(
            publicKey.decode(),
            if (store.privateKey() == null) BytesUtil.EMPTY else store.privateKey().key()
        )

        this.delegate.put(
            seq,
            putStore.toBytes()
        )
    }

    override operator fun get(seq: ByteArray): KeyPair {
        return cache().get(
            seq,
            this::createKeypair
        )
    }

    fun createKeypair(seq: ByteArray): KeyPair {
        val pubkey = publicKey(seq)

        return KeyPair(
            pubkey,
            null
        )
    }

    fun privateKey(seq: ByteArray): ByteArray {
        val store = createStore(seq).privateKey()
        store.decode(true)
        return store.key()
    }

    fun publicKey(seq: ByteArray): PublicKey = createStore(seq).publicKey().decode()

    fun createStore(seq: ByteArray): KeyPairStore {
        val data = this.delegate[seq] ?: return EmptyKeyPair()
        return KeyPairStore.create(BytesReader.of(data))
    }

    override fun remove(key: ByteArray) {
        cache().delete(
            key,
            this.delegate::remove
        )
    }

    fun add(store: KeyPairStore): Long {
        val nextSeq = nextSeq()
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)
        this.delegate.put(
            nextSeqByte,
            store.toBytes()
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

    fun seqAll(action: Consumer<Long>) {
        val nextSeq = nextSeq()
        if (nextSeq > 0) {
            for (seq in 0 until nextSeq) {
                action.accept(seq)
            }
        }
    }

    fun operation(action: BiConsumer<Long, KeyPair>) {
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

    fun deleteAll() {
        seqAll {
            remove(
                SkippedBase256.longToBuf(it)
            )
        }
    }
}