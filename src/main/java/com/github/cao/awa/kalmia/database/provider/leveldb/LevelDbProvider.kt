package com.github.cao.awa.kalmia.database.provider.leveldb

import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.cao.awa.kalmia.database.key.BytesKey
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.ExceptingSupplier
import org.iq80.leveldb.CompressionType
import org.iq80.leveldb.DB
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory
import java.io.File
import java.util.function.BiConsumer
import java.util.function.Supplier

class LevelDbProvider(cacheDelegate: Supplier<Map<BytesKey, ByteArray>>, path: String) :
    KeyValueBytesDatabase(cacheDelegate) {
    private val db: DB = Iq80DBFactory().open(
        File(path),
        Options().createIfMissing(true)
            .writeBufferSize(1048560 * 16)
            .compressionType(CompressionType.SNAPPY)
    )

    override fun set(key: BytesKey, value: ByteArray) {
        cache().update(
            key,
            value
        ) { k, v -> this.db.put(k.key(), v) }
    }

    override fun get(key: BytesKey): ByteArray? {
        return cache()[key, { this.db.get(key.key()) }]
    }

    override fun remove(key: BytesKey) {
        cache().delete(
            key
        ) { this.db.delete(key.key()) }
    }

    override fun close(): Boolean {
        return EntrustEnvironment.trys(
            ExceptingSupplier {
                this.db.close()
                cache().clear()
                true
            },
            ExceptingSupplier { false }
        )
    }

    override fun forEach(operator: BiConsumer<BytesKey, ByteArray>) {
        this.db.forEach {
            operator.accept(
                BytesKey(it.key),
                it.value
            )
        }
    }
}