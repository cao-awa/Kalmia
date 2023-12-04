package com.github.cao.awa.kalmia.database.provider.leveldb;

import com.github.cao.awa.kalmia.database.KeyValueDatabase;
import com.github.cao.awa.kalmia.database.provider.KeyValueDatabaseProvider;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;

public class LevelDbProvider extends KeyValueDatabaseProvider implements KeyValueDatabase {
    private final DB db;

    public LevelDbProvider(String path) throws IOException {
        super(path);
        this.db = new Iq80DBFactory().open(new File(path),
                                           new Options().createIfMissing(true)
                                                        .writeBufferSize(1048560 * 16)
                                                        .compressionType(CompressionType.SNAPPY)
        );
    }

    @Override
    public void put(byte[] key, byte[] value) {
        this.db.put(key,
                    value
        );
    }

    @Override
    public byte[] get(byte[] key) {
        return this.db.get(key);
    }

    @Override
    public void remove(byte[] key) {
        this.db.delete(key);
    }
}
