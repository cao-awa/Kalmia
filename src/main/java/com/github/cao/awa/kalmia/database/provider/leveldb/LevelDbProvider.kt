package com.github.cao.awa.kalmia.database.provider.leveldb;

import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

public class LevelDbProvider extends KeyValueBytesDatabase {
    private final DB db;

    public LevelDbProvider(Supplier<Map<byte[], byte[]>> cacheDelegate, String path) throws IOException {
        super(cacheDelegate);
        this.db = new Iq80DBFactory().open(new File(path),
                                           new Options().createIfMissing(true)
                                                        .writeBufferSize(1048560 * 16)
                                                        .compressionType(CompressionType.SNAPPY)
        );
    }

    @Override
    public void put(byte[] key, byte[] value) {
        cache().update(
                key,
                value,
                this.db :: put
        );
    }

    @Override
    public byte[] get(byte[] key) {
        return cache().get(
                key,
                this.db :: get
        );
    }

    @Override
    public void remove(byte[] key) {
        cache().delete(
                key,
                this.db :: delete
        );
    }

    public boolean close() {
        return EntrustEnvironment.trys(
                () -> {
                    this.db.close();
                    cache().clear();
                    return true;
                },
                () -> false
        );
    }
}
