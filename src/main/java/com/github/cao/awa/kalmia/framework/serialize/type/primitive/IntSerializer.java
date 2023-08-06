package com.github.cao.awa.kalmia.framework.serialize.type.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

@AutoSerializer(value = 4, target = {Integer.class, int.class})
public class IntSerializer implements BytesSerializer<Integer> {
    @Override
    public byte[] serialize(Integer i) {
        return SkippedBase256.intToBuf(i);
    }

    @Override
    public Integer deserialize(BytesReader reader) {
        return SkippedBase256.readInt(reader);
    }
}
