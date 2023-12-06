package com.github.cao.awa.kalmia.keypair.serializer.pair.ec;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.keypair.pair.ec.EcKeyPair;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;

@AutoBytesSerializer(value = 1000, target = EcKeyPair.class)
public class EcKeyPairSerializer implements BytesSerializer<EcKeyPair> {
    @Override
    public byte[] serialize(EcKeyPair target) {
        return target.toBytes();
    }

    @Override
    public EcKeyPair deserialize(BytesReader reader) {
        KeyPairStore store = KeyPairStore.create(reader);
        if (store instanceof EcKeyPair ec) {
            return ec;
        }
        return null;
    }
}
