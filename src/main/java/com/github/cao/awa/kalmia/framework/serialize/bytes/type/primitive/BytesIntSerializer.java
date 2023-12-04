package com.github.cao.awa.kalmia.framework.serialize.bytes.type.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

@AutoBytesSerializer(value = 4, target = {Integer.class, int.class})
public class BytesIntSerializer implements BytesSerializer<Integer> {
    @Override
    public byte[] serialize(Integer i) {
        return SkippedBase256.intToBuf(i);
    }

    @Override
    public Integer deserialize(BytesReader reader) {
        return SkippedBase256.readInt(reader);
    }
}
