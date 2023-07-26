package com.github.cao.awa.kalmia.user.factor;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.user.User;

import java.util.Map;
import java.util.function.Function;

public class UserFactor {
    private static final Map<Integer, Function<BytesReader, User>> factories = ApricotCollectionFactor.hashMap();

    public static void register(int id, Function<BytesReader, User> factor) {
        factories.put(id,
                      factor
        );
    }

    public static User create(int id, BytesReader reader) {
        Function<BytesReader, User> factor = factories.get(id);
        if (factor == null) {
            return null;
        }
        return factor.apply(reader);
    }
}
