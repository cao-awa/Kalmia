package com.github.cao.awa.kalmia.message.database.message

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
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.util.function.BiConsumer
import java.util.function.Consumer

class MessageDatabase(path: String) : KeyValueDatabase<BytesKey, Message?>(ApricotCollectionFactor::hashMap) {
    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    fun operation(@ShouldSkipped sessionIdentity: PureExtraIdentity, action: BiConsumer<Long, Message?>) {
        seqAll(
            sessionIdentity
        ) {
            action.accept(
                it,
                get(
                    sessionIdentity,
                    BytesKey(SkippedBase256.longToBuf(it))
                )
            )
        }
    }

    fun operation(
        @ShouldSkipped sessionIdentity: PureExtraIdentity,
        from: Long,
        to: Long,
        action: BiConsumer<Long, Message?>
    ) {
        seqAll(
            sessionIdentity,
            from,
            to
        ) {
            action.accept(
                it,
                get(
                    sessionIdentity,
                    BytesKey(SkippedBase256.longToBuf(it))
                )
            )
        }
    }

    override operator fun get(sessionIdentity: BytesKey, @ShouldSkipped seq: BytesKey): Message? {
        return this[
            identity(
                key(
                    sessionIdentity,
                    seq
                )
            )
        ]
    }

    operator fun get(sessionIdentity: PureExtraIdentity, @ShouldSkipped seq: BytesKey): Message? {
        return this[BytesKey(sessionIdentity.extras()), seq]
    }

    override operator fun get(identity: BytesKey): Message? {
        return cache()[
            identity,
            { getMessage(it) }
        ]
    }

    operator fun get(identity: LongAndExtraIdentity): Message? {
        return this[BytesKey(identity.toBytes())]
    }

    private fun getMessage(identity: BytesKey): Message? {
        val data = this.delegate[identity] ?: return null
        return Message.create(data)
    }

    fun getMessage(identity: LongAndExtraIdentity): Message? {
        return getMessage(BytesKey(identity.toBytes()))
    }

    override fun remove(identity: BytesKey) {
        cache().delete(
            identity
        ) {
            this.delegate.remove(it)
        }
    }

    fun remove(identity: LongAndExtraIdentity) {
        remove(BytesKey(identity.toBytes()))
    }

    override fun remove(@ShouldSkipped sessionIdentity: BytesKey, @ShouldSkipped seq: BytesKey) {
        val key = key(
            sessionIdentity,
            seq
        )
        remove(identity(key))
        this.delegate.remove(key)
    }

    fun markDelete(identity: LongAndExtraIdentity) {
        val source = get(identity) ?: return
        this[BytesKey(identity.toBytes())] = DeletedMessage(
            source.sender(),
            source.digest()
        )
    }

    fun markDelete(sessionIdentity: PureExtraIdentity, @ShouldSkipped seq: BytesKey) {
        markDelete(
            identity(
                key(
                    sessionIdentity,
                    seq
                )
            )
        )
    }

    fun identity(key: BytesKey): LongAndExtraIdentity {
        return LongAndExtraIdentity.read(BytesReader.of(this.delegate[key]))
    }

    fun identity(sessionIdentity: PureExtraIdentity, seq: BytesKey, messageIdentity: LongAndExtraIdentity) {
        this.delegate[key(sessionIdentity, seq)] = messageIdentity.toBytes()
    }

    fun seqAll(@ShouldSkipped sessionIdentity: PureExtraIdentity, action: Consumer<Long>) {
        val count = nextSeq(sessionIdentity) - 1
        if (count > 0) {
            var seq: Long = 0
            while (true) {
                if (seq > count) {
                    break
                }
                action.accept(seq)
                seq++
            }
        }
    }

    fun seqAll(sessionIdentity: PureExtraIdentity, from: Long, to: Long, action: Consumer<Long>) {
        val count = nextSeq(sessionIdentity) - 1
        if (count > 0) {
            val endIndex = Math.min(
                to,
                count
            )
            var seq = from
            while (true) {
                if (seq > endIndex) {
                    break
                }
                action.accept(seq)
                seq++
            }
        }
    }

    fun nextSeq(sessionIdentity: PureExtraIdentity): Long = curSeq(sessionIdentity) + 1

    fun curSeq(sessionIdentity: PureExtraIdentity): Long {
        val seqByte = this.delegate[BytesKey(sessionIdentity.extras())]
        return if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
    }

    fun curSeq(sessionIdentity: PureExtraIdentity, @ShouldSkipped seq: ByteArray) {
        this.delegate[BytesKey(sessionIdentity.extras())] = seq
    }

    fun deleteAll(sessionIdentity: PureExtraIdentity) {
        seqAll(
            sessionIdentity
        ) {
            markDelete(
                sessionIdentity,
                BytesKey(SkippedBase256.longToBuf(it))
            )
        }
    }

    fun key(sessionIdentity: BytesKey, @ShouldSkipped seq: BytesKey): BytesKey {
        return BytesKey(
            BytesUtil.concat(
                sessionIdentity.key(),
                seq.key()
            )
        )
    }

    fun key(sessionIdentity: PureExtraIdentity, @ShouldSkipped seq: BytesKey): BytesKey {
        return key(BytesKey(sessionIdentity.extras()), seq)
    }

    fun search(sessionIdentity: PureExtraIdentity, target: String): Set<Long> {
        val result: Set<Long> = ApricotCollectionFactor.hashSet()
        operation(
            sessionIdentity
        ) { _, _ -> }
        return result
    }

    override fun set(identity: BytesKey, msg: Message?) {
        cache().update(
            identity,
            msg,
            this::update
        )
    }

    fun set(identity: LongAndExtraIdentity, msg: Message) {
        this[BytesKey(identity.toBytes())] = msg
    }

    private fun update(identity: BytesKey, msg: Message?) {
        if (msg == null) {
            this.remove(identity)
        } else {
            this.delegate[identity] = msg.toBytes()
        }
    }

    fun send(sessionIdentity: PureExtraIdentity, msg: Message): Long {
        val nextSeq = nextSeq(sessionIdentity)
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)

        // Save the redirect to global id.
        identity(
            sessionIdentity,
            BytesKey(nextSeqByte),
            msg.identity()
        )

        // Update index.
        curSeq(
            sessionIdentity,
            nextSeqByte
        )

        // Update message.
        set(
            msg.identity(),
            msg
        )

        // Return two identity result.
        return nextSeq
    }
}