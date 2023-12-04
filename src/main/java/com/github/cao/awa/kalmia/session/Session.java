package com.github.cao.awa.kalmia.session;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.convert.BytesValueConvertable;
import com.github.cao.awa.kalmia.session.factor.SessionFactor;

public abstract class Session implements BytesValueConvertable {
    private final long sessionId;

    public Session(long sessionId) {
        this.sessionId = sessionId;
    }

    public long sessionId() {
        return this.sessionId;
    }

    public abstract boolean accessible(long userId);

    public abstract byte[] header();

    public static Session create(byte[] data) {
        BytesReader reader = BytesReader.of(data);

        int id = reader.read();

        reader.back(1);

        return SessionFactor.create(id,
                                    reader
        );
    }
}
