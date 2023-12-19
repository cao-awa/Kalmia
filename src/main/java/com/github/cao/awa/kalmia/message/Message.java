package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.convert.ByteArrayConvertable;
import com.github.cao.awa.kalmia.digest.DigestedObject;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.message.display.ClientMessageContent;
import com.github.cao.awa.kalmia.message.factor.MessageFactor;
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage;

public abstract class Message implements DigestedObject, ByteArrayConvertable {
    private final LongAndExtraIdentity identity;

    public Message(LongAndExtraIdentity identity) {
        this.identity = identity;
    }

    public Message() {
        this.identity = LongAndExtraIdentity.create(TimeUtil.millions(),
                                                    BytesRandomIdentifier.create(22)
        );
    }

    public abstract LongAndExtraIdentity sender();

    public LongAndExtraIdentity identity() {
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
