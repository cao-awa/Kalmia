package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.convert.ByteArrayConvertable;
import com.github.cao.awa.kalmia.digest.DigestedObject;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.message.factor.MessageFactor;

public abstract class Message implements DigestedObject, ByteArrayConvertable {
    public abstract long getSender();

    public static Message create(byte[] data) {
        return create(BytesReader.of(data));
    }

    public static Message create(BytesReader reader) {
        return MessageFactor.create(
                reader.read(),
                reader.back(1)
        );
    }

    public byte[] toBytes() {
        try {
            return KalmiaEnv.serializeFramework.payload(this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
