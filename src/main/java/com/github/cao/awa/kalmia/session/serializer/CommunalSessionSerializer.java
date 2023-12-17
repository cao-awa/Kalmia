package com.github.cao.awa.kalmia.session.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.session.communal.CommunalSession;

@AutoBytesSerializer(value = 3000, target = CommunalSession.class)
public class CommunalSessionSerializer implements BytesSerializer<CommunalSession> {
    @Override
    public byte[] serialize(CommunalSession target) {
        return target.bytes();
    }

    @Override
    public CommunalSession deserialize(BytesReader reader) {
        return CommunalSession.create(reader);
    }
}
