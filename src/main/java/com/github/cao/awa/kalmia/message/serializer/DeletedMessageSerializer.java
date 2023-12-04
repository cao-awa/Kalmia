package com.github.cao.awa.kalmia.message.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage;

@AutoBytesSerializer(value = 1000, target = DeletedMessage.class)
public class DeletedMessageSerializer implements BytesSerializer<DeletedMessage> {
    @Override
    public byte[] serialize(DeletedMessage target) {
        return target.toBytes();
    }

    @Override
    public DeletedMessage deserialize(BytesReader reader) {
        return DeletedMessage.create(reader);
    }
}
