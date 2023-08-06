package com.github.cao.awa.kalmia.message.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.BytesSerializer;
import com.github.cao.awa.kalmia.message.plains.PlainsMessage;

@AutoSerializer(value = 1001, target = PlainsMessage.class)
public class PlainsMessageSerializer implements BytesSerializer<PlainsMessage> {
    @Override
    public byte[] serialize(PlainsMessage plainMessage) {
        return plainMessage.toBytes();
    }

    @Override
    public PlainsMessage deserialize(BytesReader reader) {
        return PlainsMessage.create(reader);
    }
}
