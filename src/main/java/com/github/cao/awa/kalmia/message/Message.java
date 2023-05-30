package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.digest.DigestedObject;

public abstract class Message implements DigestedObject {
    public abstract byte[] toBytes();

    public abstract long getSender();

    public static Message create(byte[] data) {
        PlainMessage plain = createPlain(data);
        if (plain == null) {
            return createDeleted(data);
        }
        return plain;
    }

    public static DeletedMessage createDeleted(byte[] data) {
        return createDeleted(new BytesReader(data));
    }

    public static PlainMessage createPlain(byte[] data) {
        return createPlain(new BytesReader(data));
    }

    public static Message create(BytesReader reader) {
        reader.flag();
        PlainMessage plain = createPlain(reader);
        if (plain == null) {
            reader.back();
            return createDeleted(reader);
        }
        return plain;
    }

    public static DeletedMessage createDeleted(BytesReader reader) {
        return DeletedMessage.create(reader);
    }

    public static PlainMessage createPlain(BytesReader reader) {
        return PlainMessage.create(reader);
    }
}
