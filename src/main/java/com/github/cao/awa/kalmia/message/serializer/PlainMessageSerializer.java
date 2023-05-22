package com.github.cao.awa.kalmia.message.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.message.PlainMessage;

@AutoSerializer(value = 1001, target = PlainMessage.class)
public class PlainMessageSerializer implements BytesSerializer<PlainMessage> {
    @Override
    public byte[] serialize(PlainMessage plainMessage) throws Exception {
        return plainMessage.toBytes();
    }

    @Override
    public PlainMessage deserialize(BytesReader reader) throws Exception {
        return PlainMessage.create(reader);
    }

    @Override
    public PlainMessage initializer() {
        return null;
    }
}
