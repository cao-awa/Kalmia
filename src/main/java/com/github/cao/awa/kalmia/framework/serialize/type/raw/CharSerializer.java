package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.Base256;

@AutoSerializer(value = 2, target = Character.class)
public class CharSerializer implements BytesSerializer<Character> {
    @Override
    public byte[] serialize(Character c) {
        return Base256.tagToBuf(c);
    }

    @Override
    public Character deserialize(BytesReader reader) {
        return (char) Base256.tagFromBuf(reader.read(2));
    }

    @Override
    public Character initializer() {
        return (char) - 1;
    }
}