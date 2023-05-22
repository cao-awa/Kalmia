package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

@AutoSerializer(value = 10, target = String.class)
public class StringSerializer implements BytesSerializer<String> {
    @Override
    public byte[] serialize(String str) {
        return BytesUtil.concat(SkippedBase256.intToBuf(str.length()),
                                str.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String deserialize(BytesReader reader) {
        return new String(reader.read(SkippedBase256.readInt(reader)),
                          StandardCharsets.UTF_8
        );
    }

    @Override
    public String initializer() {
        return "";
    }
}
