package com.github.cao.awa.kalmia.framework.serialize.bytes.type.map;

import com.github.cao.awa.apricot.annotation.Unsupported;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;

import java.util.Map;

@Unsupported
@AutoSerializer(value = 501, target = Map.class)
public class MapSerializer implements BytesSerializer<Map<?, ?>> {
    @Override
    public byte[] serialize(Map<?, ?> map) {
        return null;
    }

    @Override
    public Map<?, ?> deserialize(BytesReader element) {
        return null;
    }
}
