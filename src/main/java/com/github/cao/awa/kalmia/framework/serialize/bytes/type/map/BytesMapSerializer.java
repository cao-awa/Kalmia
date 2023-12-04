package com.github.cao.awa.kalmia.framework.serialize.bytes.type.map;

import com.github.cao.awa.apricot.annotations.Unsupported;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;

import java.util.Map;

@Unsupported
@AutoBytesSerializer(value = 501, target = Map.class)
public class BytesMapSerializer implements BytesSerializer<Map<?, ?>> {
    @Override
    public byte[] serialize(Map<?, ?> map) {
        return null;
    }

    @Override
    public Map<?, ?> deserialize(BytesReader element) {
        return null;
    }
}
