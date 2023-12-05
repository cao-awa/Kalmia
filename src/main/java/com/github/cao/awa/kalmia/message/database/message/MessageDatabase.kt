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
            gid(
                key(
                    sid,
                    seq
                )
            )
        )
    }

    override operator fun get(gid: ByteArray): Message {
        return cache().get(
            gid
        ) {
            getMessage(it)
        }
    }

    private fun getMessage(gid: ByteArray): Message? {
        val data = this.delegate[gid] ?: return null;
        return Message.create(data)
    }

    override fun remove(gid: ByteArray) {
        cache().delete(
            gid
        ) {
            this.delegate.remove(it)
        }
    }

    override fun remove(@ShouldSkipped sid: ByteArray, @ShouldSkipped seq: ByteArray) {
        val key = key(
            sid,
            seq
        )
        remove(gid(key))
        this.delegate.remove(key)
    }

    fun markDelete(gid: ByteArray) {
        val source = get(gid)
        put(
            gid,
            DeletedMessage(
                source.sender(),
                source.digest()
            )
        )
    }

    fun markDelete(@ShouldSkipped sid: ByteArray, @ShouldSkipped seq: ByteArray) {
        markDelete(
            gid(
                key(
                    sid,
                    seq
                )
            )
        )
    }

    fun gid(key: ByteArray): ByteArray {
        return this.delegate[key]
    }

    fun gid(sid: ByteArray, seq: ByteArray, gid: ByteArray) {
        this.delegate.put(
            key(
                sid,
                seq
            ),
            gid
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

    override fun put(gid: ByteArray, msg: Message) {
        cache().update(
            gid,
            msg,
            this::update
        )
    }

    private fun update(gid: ByteArray, msg: Message) {
        this.delegate.put(
            gid,
            msg.toBytes()
        )
    }

    fun send(@ShouldSkipped sid: ByteArray, msg: Message): Long {
        val nextSeq = curSeq(sid)
        val nextSeqByte = SkippedBase256.longToBuf(nextSeq)

        // Save the redirector to global id.
        gid(
            sid,
            nextSeqByte,
            msg.globalId()
        )

        // Update index.
        curSeq(
            sid,
            nextSeqByte
        )

        // Update message.
        put(
            msg.globalId(),
            msg
        )

        // Return two identity result.
        return nextSeq
    }
}