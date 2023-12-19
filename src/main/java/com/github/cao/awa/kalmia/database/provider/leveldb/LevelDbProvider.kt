package com.github.cao.awa.kalmia.database.provider.leveldb

import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.ExceptingSupplier
import org.iq80.leveldb.CompressionType
import org.iq80.leveldb.DB
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory
import java.io.File
import java.util.function.BiConsumer
import java.util.function.Supplier

class LevelDbProvider(cacheDelegate: Supplier<Map<ByteArray, ByteArray>>, path: String) :
    KeyValueBytesDatabase(cacheDelegate) {
    private val db: DB

    init {
        this.db = Iq80DBFactory().open(
            File(path),
            Options().createIfMissing(true)
                .writeBufferSize(1048560 * 16)
                .compressionType(CompressionType.SNAPPY)
        )
    }

    override fun set(key: ByteArray, value: ByteArray) {
        cache().update(
            key,
            value,
            this.db::put
        )
    }

    override fun get(key: ByteArray): ByteArray? {
        return cache()[key, this.db::get]
    }

    override fun remove(key: ByteArray) {
        cache().delete(
            key,
            this.db::delete
        )
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

    override fun forEach(operator: BiConsumer<ByteArray, ByteArray>) {
        this.db.forEach {
            operator.accept(
                it.key,
                it.value
            )
        }
    }
}