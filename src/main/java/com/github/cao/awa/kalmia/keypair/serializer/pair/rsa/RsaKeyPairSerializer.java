package com.github.cao.awa.kalmia.keypair.serializer.pair.rsa;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.keypair.pair.rsa.RsaKeyPair;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;

@AutoBytesSerializer(value = 2001, target = RsaKeyPair.class)
public class RsaKeyPairSerializer implements BytesSerializer<RsaKeyPair> {
    @Override
    public byte[] serialize(RsaKeyPair target) {
        return target.toBytes();
    }

    @Override
    public RsaKeyPair deserialize(BytesReader reader) {
        KeyPairStore store = KeyPairStore.create(reader);
        if (store instanceof RsaKeyPair rsa) {
            return rsa;
        }
        return null;
    }
}
