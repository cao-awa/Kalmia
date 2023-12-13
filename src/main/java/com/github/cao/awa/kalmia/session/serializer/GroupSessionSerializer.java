package com.github.cao.awa.kalmia.session.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.session.types.group.GroupSession;

@AutoBytesSerializer(value = 3001, target = GroupSession.class)
public class GroupSessionSerializer implements BytesSerializer<GroupSession> {
    @Override
    public byte[] serialize(GroupSession target) {
        return target.bytes();
    }

    @Override
    public GroupSession deserialize(BytesReader reader) {
        return GroupSession.create(reader);
    }
}
