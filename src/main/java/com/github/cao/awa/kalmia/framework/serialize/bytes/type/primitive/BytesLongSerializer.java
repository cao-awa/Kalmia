package com.github.cao.awa.kalmia.framework.serialize.bytes.type.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

@AutoBytesSerializer(value = 5, target = {Long.class, long.class})
public class BytesLongSerializer implements BytesSerializer<Long> {
    @Override
    public byte[] serialize(Long l) {
        return SkippedBase256.longToBuf(l);
    }

    @Override
    public Long deserialize(BytesReader reader) {
        return SkippedBase256.readLong(reader);
    }
}
