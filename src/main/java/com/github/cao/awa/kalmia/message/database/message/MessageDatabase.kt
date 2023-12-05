package com.github.cao.awa.kalmia.message.database.message

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.annotations.number.encode.ShouldSkipped
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.KeyValueDatabase
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage
import com.github.cao.awa.kalmia.message.identity.MessageIdentity
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import java.util.function.BiConsumer
import java.util.function.Consumer

class MessageDatabase(path: String) : KeyValueDatabase<ByteArray, Message>(ApricotCollectionFactor::hashMap) {
    private val delegate: KeyValueBytesDatabase

    init {
        this.delegate = DatabaseProviders.bytes(path)
    }

    fun operation(@ShouldSkipped sid: ByteArray, action: BiConsumer<Long, Message>) {
        seqAll(
            sid
        ) {
            action.accept(
                it,
                get(
                    sid,
                    SkippedBase256.longToBuf(it)
                )
            )
        }
    }

    fun operation(@ShouldSkipped sid: ByteArray, from: Long, to: Long, action: BiConsumer<Long, Message>) {
        seqAll(
            sid,
            from,
            to
        ) {
            action.accept(
                it,
                get(
                    sid,
                    SkippedBase256.longToBuf(it)
                )
            )
        }
    }

    override operator fun get(@ShouldSkipped sid: ByteArray, @ShouldSkipped seq: ByteArray): Message {
        return get(
            identity(
                key(
                    sid,
                    seq
                )
            )
        )
    }

    override operator fun get(identity: ByteArray): Message {
        return cache().get(
            identity
        ) {
            getMessage(it)
        }
    }

    operator fun get(identity: MessageIdentity): Message {
        return get(identity.toBytes())
    }

    private fun getMessage(identity: ByteArray): Message? {
        val data = this.delegate[identity] ?: return null;
        return Message.create(data)
    }

    fun getMessage(identity: MessageIdentity): Message? {
        return getMessage(identity.toBytes())
    }

    override fun remove(identity: ByteArray) {
        cache().delete(
            identity
        ) {
            this.delegate.remove(it)
        }
    }

    fun remove(identity: MessageIdentity) {
        remove(identity.toBytes())
    }

    override fun remove(@ShouldSkipped sid: ByteArray, @ShouldSkipped seq: ByteArray) {
        val key = key(
            sid,
            seq
        )
        remove(identity(key))
        this.delegate.remove(key)
    }

    fun markDelete(identity: MessageIdentity) {
        val source = get(identity)
        put(
            identity.toBytes(),
            DeletedMessage(
                source.sender(),
                source.digest()
            )
        )
    }

    fun markDelete(@ShouldSkipped sid: ByteArray, @ShouldSkipped seq: ByteArray) {
        markDelete(
            identity(
                key(
                    sid,
                    seq
                )
            )
        )
    }

    fun identity(key: ByteArray): MessageIdentity {
        return MessageIdentity.create(BytesReader.of(this.delegate[key]))
    }

    fun identity(sid: ByteArray, seq: ByteArray, messageIdentity: MessageIdentity) {
        this.delegate.put(
            key(
                sid,
                seq
            ),
            messageIdentity.toBytes()
        )
    }

    fun seqAll(@ShouldSkipped sid: ByteArray, action: Consumer<Long>) {
        val seqByte = this.delegate[sid]
        var count = if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
        count++
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

    fun seqAll(@ShouldSkipped sid: ByteArray, from: Long, to: Long, action: Consumer<Long>) {
        val count = curSeq(sid)
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

    fun present(key: ByteArray): Boolean {
        return this.delegate[key] != null
    }

    fun seq(@ShouldSkipped sid: ByteArray): Long {
        val seqByte = this.delegate[sid]
        return if (seqByte == null) -1 else SkippedBase256.readLong(BytesReader.of(seqByte))
    }

    fun curSeq(@ShouldSkipped sid: ByteArray): Long {
        val seqByte = this.delegate[sid]
        return if (seqByte == null) 0 else SkippedBase256.readLong(BytesReader.of(seqByte)) + 1
    }

    fun curSeq(@ShouldSkipped sid: ByteArray, @ShouldSkipped seq: ByteArray) {
        this.delegate.put(
            sid,
            seq
        )
    }

    fun deleteAll(@ShouldSkipped sid: ByteArray) {
        seqAll(
            sid
        ) {
            markDelete(
                sid,
                SkippedBase256.longToBuf(it)
            )
        }
    }

    fun key(@ShouldSkipped sid: ByteArray, @ShouldSkipped seq: ByteArray): ByteArray {
        return BytesUtil.concat(
            sid,
            seq
        )
    }

    fun search(@ShouldSkipped sid: ByteArray, target: String): Set<Long> {
        val result: Set<Long> = ApricotCollectionFactor.hashSet()
        operation(
            sid
        ) { seq: Long, msg: Message -> }
        return result
    }

    override fun put(identity: ByteArray, msg: Message) {
        cache().update(
            identity,
            msg,
            this::update
        )
    }

    fun put(identity: MessageIdentity, msg: Message) {
        put(
            identity.toBytes(),
            msg
        )
    }

    private fun update(identity: ByteArray, msg: Message) {
        this.delegate.put(
            identity,
            msg.toBytes()
        )
    }

    fun send(@ShouldSkipped sid: ByteArray, msg: Message): Long {
        val nextSeq = curSeq(sid)
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)

        // Save the redirector to global id.
        identity(
            sid,
            nextSeqByte,
            msg.identity()
        )

        // Update index.
        curSeq(
            sid,
            nextSeqByte
        )

        // Update message.
        put(
            msg.identity(),
            msg
        )

        // Return two identity result.
        return nextSeq
    }
}