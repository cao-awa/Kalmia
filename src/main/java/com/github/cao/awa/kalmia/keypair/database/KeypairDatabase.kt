package com.github.cao.awa.kalmia.keypair.database

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.key.BytesKey
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity
import com.github.cao.awa.kalmia.keypair.pair.empty.EmptyKeyPair
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.util.function.BiConsumer

class KeypairDatabase(path: String?) : KeyValueDatabase<BytesKey, KeyPairStore?>(ApricotCollectionFactor::hashMap) {
    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    override operator fun set(identity: BytesKey, keypair: KeyPairStore?) {
        if (keypair == null) {
            remove(identity)
            return
        }

        keypair.privateKey().decode(true)

        putPublic(
            identity,
            keypair.publicKey()
        )

        putPrivate(
            identity,
            keypair.privateKey()
        )
    }

    operator fun set(identity: PureExtraIdentity, keypair: KeyPairStore?) {
        this[BytesKey(identity.extras())] = keypair
    }

    fun putPrivate(identity: BytesKey, privateKey: KeyStore<out PrivateKey>) {
        // Ensure public key is unable to decode, should be crypted.
        privateKey.decode(true)

        val store = createStore(identity)
        val putStore = KeyStoreIdentity.createKeyPairStore(
            store.identity(),
            store.publicKey().decode(),
            privateKey.key()
        )

        this.delegate[identity] = putStore.toBytes()
    }

    fun putPublic(identity: BytesKey, publicKey: KeyStore<out PublicKey>) {
        // Ensure public key is able to decode.
        val decoded = publicKey.decode()

        val store = createStore(identity)
        val putStore = KeyStoreIdentity.createKeyPairStore(
            store.identity(),
            decoded,
            store.privateKey().key()
        )

        this.delegate[identity] = putStore.toBytes()
    }

    override operator fun get(identity: BytesKey): KeyPairStore? {
        return cache()[identity, this::createStore]
    }

    fun privateKey(identity: BytesKey): ByteArray {
        val store = createStore(identity).privateKey()
        store.decode(true)
        return store.key()
    }

    fun publicKey(seq: BytesKey): PublicKey? = createStore(seq).publicKey().decode()

    fun createStore(identity: BytesKey): KeyPairStore {
        val data = this.delegate[identity] ?: return EmptyKeyPair()
        return KeyPairStore.create(BytesReader.of(data))
    }

    override fun remove(key: BytesKey) {
        cache().delete(
            key,
            this.delegate::remove
        )
    }

    fun add(store: KeyPairStore): PureExtraIdentity {
        val identity = PureExtraIdentity.create(BytesRandomIdentifier.create(16))
        this[identity] = store
        return identity
    }

    fun operation(action: BiConsumer<PureExtraIdentity, KeyPairStore?>) {
        forEach { _, v ->
            if (v != null) {
                action.accept(
                    v.identity(),
                    v
                )
            }
        }
    }

    fun deleteAll() {
        forEach { k, _ ->
            remove(k)
        }
    }

    override fun forEach(operator: BiConsumer<BytesKey, KeyPairStore?>) {
        this.delegate.forEach { k, v ->
            val store = KeyPairStore.create(BytesReader.of(v))
            operator.accept(
                k,
                store
            )
        }
    }
}