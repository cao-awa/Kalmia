package com.github.cao.awa.kalmia.keypair.database

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity
import com.github.cao.awa.kalmia.keypair.pair.empty.EmptyKeyPair
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import java.security.PrivateKey
import java.security.PublicKey
import java.util.function.BiConsumer
import java.util.function.Consumer

class KeypairDatabase(path: String?) : KeyValueDatabase<ByteArray, KeyPairStore?>(ApricotCollectionFactor::hashMap) {
    companion object {
        private val ROOT = byteArrayOf(1)
    }

    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    override fun set(seq: ByteArray, keypair: KeyPairStore?) {
        if (keypair == null) {
            remove(seq)
            return
        }

        keypair.privateKey().decode(true)

        putPublic(
            seq,
            keypair.publicKey()
        )

        putPrivate(
            seq,
            keypair.privateKey()
        )
    }

    fun putPrivate(seq: ByteArray, privateKey: KeyStore<out PrivateKey>) {
        // Ensure public key is unable to decode, should be crypted.
        privateKey.decode(true)

        val store = createStore(seq)
        val putStore = KeyStoreIdentity.createKeyPairStore(
            store.publicKey().decode(),
            privateKey.key()
        )

        this.delegate[seq] = putStore.toBytes()
    }

    fun putPublic(seq: ByteArray, publicKey: KeyStore<out PublicKey>) {
        // Ensure public key is able to decode.
        val decoded = publicKey.decode()

        val store = createStore(seq)
        val putStore = KeyStoreIdentity.createKeyPairStore(
            decoded,
            store.privateKey().key()
        )

        this.delegate[seq] = putStore.toBytes()
    }

    override operator fun get(seq: ByteArray): KeyPairStore? {
        return cache()[seq, this::createStore]
    }

    fun privateKey(seq: ByteArray): ByteArray {
        val store = createStore(seq).privateKey()
        store.decode(true)
        return store.key()
    }

    fun publicKey(seq: ByteArray): PublicKey? = createStore(seq).publicKey().decode()

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
        this[nextSeqByte] = store
        this.delegate[ROOT] = nextSeqByte
        return nextSeq
    }

    fun seq(): Long {
        val seqByte = this.delegate[ROOT]
        return if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
    }

    fun nextSeq(): Long = seq() + 1

    fun seqAll(action: Consumer<Long>) {
        val nextSeq = nextSeq()
        if (nextSeq > 0) {
            for (seq in 0 until nextSeq) {
                action.accept(seq)
            }
        }
    }

    fun operation(action: BiConsumer<Long, KeyPairStore?>) {
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