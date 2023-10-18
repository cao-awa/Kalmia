package com.github.cao.awa.kalmia.message.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage;

@AutoSerializer(value = 1002, target = UnknownMessage.class)
public class UnknownMessageSerializer implements BytesSerializer<UnknownMessage> {
    @Override
    public byte[] serialize(UnknownMessage target) {
        return target.toBytes();
    }

    @Override
    public UnknownMessage deserialize(BytesReader reader) {
        return UnknownMessage.create(reader);
    }
}
