package com.github.cao.awa.kalmia.message.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.message.cover.CoverMessage;

@AutoBytesSerializer(value = 1003, target = CoverMessage.class)
public class CoverMessageSerializer implements BytesSerializer<CoverMessage> {
    @Override
    public byte[] serialize(CoverMessage target) {
        return target.toBytes();
    }

    @Override
    public CoverMessage deserialize(BytesReader reader) {
        return CoverMessage.create(reader);
    }
}
