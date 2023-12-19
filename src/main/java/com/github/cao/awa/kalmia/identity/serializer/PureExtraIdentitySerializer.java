package com.github.cao.awa.kalmia.identity.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;

@AutoBytesSerializer(value = 4000, target = PureExtraIdentity.class)
public class PureExtraIdentitySerializer implements BytesSerializer<PureExtraIdentity> {
    @Override
    public byte[] serialize(PureExtraIdentity target) {
        return target.toBytes();
    }

    @Override
    public PureExtraIdentity deserialize(BytesReader reader) {
        return PureExtraIdentity.read(reader);
    }
}
