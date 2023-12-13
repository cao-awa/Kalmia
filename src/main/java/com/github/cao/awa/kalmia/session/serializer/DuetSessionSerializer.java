package com.github.cao.awa.kalmia.session.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.session.types.duet.DuetSession;

@AutoBytesSerializer(value = 3002, target = DuetSession.class)
public class DuetSessionSerializer implements BytesSerializer<DuetSession> {
    @Override
    public byte[] serialize(DuetSession target) {
        return target.bytes();
    }

    @Override
    public DuetSession deserialize(BytesReader reader) {
        return DuetSession.create(reader);
    }
}
