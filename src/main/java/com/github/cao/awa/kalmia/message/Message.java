package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.message.digest.MessageDigest;

public abstract class Message {
    public abstract byte[] toBytes();
    public abstract MessageDigest getDigest();

    public abstract long getSender();

    public static Message create(byte[] data) {
        PlainMessage plain = createPlain(data);
        if (plain == null) {
            return createDeleted(data);
        }
        return plain;
    }

    public static DeletedMessage createDeleted(byte[] data) {
        return DeletedMessage.create(new BytesReader(data));
    }

    public static PlainMessage createPlain(byte[] data) {
        return PlainMessage.create(new BytesReader(data));
    }
}
