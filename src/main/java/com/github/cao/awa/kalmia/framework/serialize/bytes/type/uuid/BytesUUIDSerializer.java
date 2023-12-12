package com.github.cao.awa.kalmia.framework.serialize.bytes.type.uuid;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.util.UUID;

@AutoBytesSerializer(value = 11, target = UUID.class)
public class BytesUUIDSerializer implements BytesSerializer<UUID> {
    @Override
    public byte[] serialize(UUID uuid) {
        return BytesUtil.concat(
                Base256.longToBuf(uuid.getMostSignificantBits()),
                Base256.longToBuf(uuid.getLeastSignificantBits())
        );
    }

    @Override
    public UUID deserialize(BytesReader reader) {
        return new UUID(Base256.readLong(reader),
                        Base256.readLong(reader)
        );
    }
}
