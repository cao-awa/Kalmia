package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.convert.ByteArrayConvertable;
import com.github.cao.awa.kalmia.digest.DigestedObject;
import com.github.cao.awa.kalmia.message.display.ClientMessageContent;
import com.github.cao.awa.kalmia.message.factor.MessageFactor;
import com.github.cao.awa.kalmia.message.identity.MessageIdentity;
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage;

public abstract class Message implements DigestedObject, ByteArrayConvertable {
    private final MessageIdentity identity;

    public Message(MessageIdentity identity) {
        this.identity = identity;
    }

    public Message() {
        this.identity = MessageIdentity.create(BytesRandomIdentifier.create(24));
    }

    public abstract long sender();

    public MessageIdentity identity() {
        return this.identity;
    }

    public abstract byte[] header();

    public abstract ClientMessageContent display();

    public static Message create(byte[] data) {
        return create(BytesReader.of(data));
    }

    public static Message create(BytesReader reader) {
        try {
            return MessageFactor.create(
                    reader.read(),
                    reader.back(1)
            );
        } catch (Exception e) {
            return new UnknownMessage(
                    reader.reset()
                          .all()
            );
        }
    }
}
