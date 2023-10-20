package com.github.cao.awa.kalmia.session.factor;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.session.Session;

import java.util.Map;
import java.util.function.Function;

public class SessionFactor {
    private static final Map<Integer, Function<BytesReader, Session>> factories = ApricotCollectionFactor.hashMap();

    public static void register(int id, Function<BytesReader, Session> factor) {
        factories.put(id,
                      factor
        );
    }

    public static Session create(int id, BytesReader reader) {
        Function<BytesReader, Session> factor = factories.get(id);
        if (factor == null) {
            return null;
        }
        return factor.apply(reader);
    }
}
