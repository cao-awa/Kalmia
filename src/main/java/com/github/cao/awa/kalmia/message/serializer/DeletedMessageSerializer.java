package com.github.cao.awa.kalmia.message.serializer;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.message.DeletedMessage;

@AutoSerializer(value = 1000, target = DeletedMessage.class)
public class DeletedMessageSerializer implements BytesSerializer<DeletedMessage> {
    @Override
    public byte[] serialize(DeletedMessage plainMessage) throws Exception {
        return plainMessage.toBytes();
    }

    @Override
    public DeletedMessage deserialize(BytesReader reader) throws Exception {
        return DeletedMessage.create(reader);
    }

    @Override
    public DeletedMessage initializer() {
        return null;
    }
}
