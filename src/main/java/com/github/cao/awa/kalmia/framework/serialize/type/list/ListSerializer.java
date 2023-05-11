package com.github.cao.awa.kalmia.framework.serialize.type.list;

import com.github.cao.awa.apricot.anntation.Unsupported;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Unsupported
public class ListSerializer implements BytesSerializer<List<?>> {
    @Override
    public byte[] serialize(List<?> objects) throws Exception {
        if (objects.size() < 1) {
            return BytesUtil.EMPTY;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        return output.toByteArray();
    }

    @Override
    public List<?> deserialize(BytesReader reader) {
        return null;
    }

    @Override
    public List<?> initializer() {
        return ApricotCollectionFactor.newArrayList();
    }

    @Override
    public long id() {
        return 500;
    }
}
