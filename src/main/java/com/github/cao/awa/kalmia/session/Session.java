package com.github.cao.awa.kalmia.session;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.session.duet.DuetSession;

public abstract class Session {
    public abstract byte[] toBytes();

    public abstract boolean accessible(long userId);

    public static Session create(byte[] data) {
        BytesReader reader = BytesReader.of(data);

        int id = reader.read();

        reader.back(1);

        return switch (id) {
            case 1 -> DuetSession.create(reader);
            default -> null;
        };
    }
}
