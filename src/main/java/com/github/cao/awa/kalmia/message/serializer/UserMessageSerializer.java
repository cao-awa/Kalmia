package com.github.cao.awa.kalmia.message.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.message.user.UserMessage;

@AutoBytesSerializer(value = 1002, target = UserMessage.class)
public class UserMessageSerializer implements BytesSerializer<UserMessage> {
    @Override
    public byte[] serialize(UserMessage target) {
        return target.toBytes();
    }

    @Override
    public UserMessage deserialize(BytesReader reader) {
        return UserMessage.create(reader);
    }
}
