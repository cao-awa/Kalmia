package com.github.cao.awa.kalmia.message.factor;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.message.Message;

import java.util.Map;
import java.util.function.Function;

public class MessageFactor {
    private static final Map<Integer, Function<BytesReader, Message>> factories = ApricotCollectionFactor.hashMap();

    public static void register(int id, Function<BytesReader, Message> factor) {
        factories.put(id,
                      factor
        );
    }

    public static Message create(int id, BytesReader reader) {
        Function<BytesReader, Message> factor = factories.get(id);
        if (factor == null) {
            return null;
        }
        return factor.apply(reader);
    }
}
