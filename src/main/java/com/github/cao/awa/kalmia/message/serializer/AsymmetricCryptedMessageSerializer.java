package com.github.cao.awa.kalmia.message.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.message.crypt.AsymmetricCryptedMessage;

@AutoBytesSerializer(value = 1003, target = AsymmetricCryptedMessage.class)
public class AsymmetricCryptedMessageSerializer implements BytesSerializer<AsymmetricCryptedMessage> {
    @Override
    public byte[] serialize(AsymmetricCryptedMessage target) {
        return target.toBytes();
    }

    @Override
    public AsymmetricCryptedMessage deserialize(BytesReader reader) {
        return AsymmetricCryptedMessage.create(reader);
    }
}
