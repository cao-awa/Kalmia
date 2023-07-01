package com.github.cao.awa.kalmia.framework.serialize.type.raw.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

@AutoSerializer(value = 4, target = {Integer.class, int.class})
public class IntSerializer implements BytesSerializer<Integer> {
    @Override
    public byte[] serialize(Integer integer) throws Exception {
        return SkippedBase256.intToBuf(integer);
    }

    @Override
    public Integer deserialize(BytesReader reader) throws Exception {
        return SkippedBase256.readInt(reader);
    }

    @Override
    public Integer initializer() {
        return - 1;
    }
}
