package com.github.cao.awa.kalmia.database;

import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;

public class LevelDbProvide extends KeyValueDatabaseProvide implements KeyValueDatabase {
    private final DB db;

    public LevelDbProvide(String path) throws IOException {
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
}
