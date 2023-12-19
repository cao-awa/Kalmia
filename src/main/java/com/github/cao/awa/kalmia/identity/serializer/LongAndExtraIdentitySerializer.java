package com.github.cao.awa.kalmia.identity.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;

@AutoBytesSerializer(value = 4001, target = LongAndExtraIdentity.class)
public class LongAndExtraIdentitySerializer implements BytesSerializer<LongAndExtraIdentity> {
    @Override
    public byte[] serialize(LongAndExtraIdentity target) {
        return target.toBytes();
    }

    @Override
    public LongAndExtraIdentity deserialize(BytesReader reader) {
        return LongAndExtraIdentity.read(reader);
    }
}
