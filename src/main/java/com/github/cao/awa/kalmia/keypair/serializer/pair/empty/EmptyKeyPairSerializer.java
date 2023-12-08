package com.github.cao.awa.kalmia.keypair.serializer.pair.empty;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.keypair.pair.empty.EmptyKeyPair;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;

@AutoBytesSerializer(value = 2123, target = EmptyKeyPair.class)
public class EmptyKeyPairSerializer implements BytesSerializer<EmptyKeyPair> {
    @Override
    public byte[] serialize(EmptyKeyPair target) {
        return target.toBytes();
    }

    @Override
    public EmptyKeyPair deserialize(BytesReader reader) {
        KeyPairStore store = KeyPairStore.create(reader);
        if (store instanceof EmptyKeyPair empty) {
            return empty;
        }
        return null;
    }
}
