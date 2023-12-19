package com.github.cao.awa.kalmia.session;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.convert.BytesValueConvertable;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.session.factor.SessionFactor;
import com.github.cao.awa.kalmia.setting.Settings;

public abstract class Session implements BytesValueConvertable {
    private final PureExtraIdentity identity;

    public Session(PureExtraIdentity identity) {
        this.identity = identity;
    }

    public PureExtraIdentity identity() {
        return this.identity;
    }

    public abstract boolean accessible(LongAndExtraIdentity accessIdentity);

    public Settings settings() {
        return Kalmia.SERVER.sessionManager()
                            .settings(identity());
    }

    public abstract byte[] header();

    public abstract String displayName();

    @Override
    public String toString() {
        return displayName();
    }

    public static Session create(byte[] data) {
        BytesReader reader = BytesReader.of(data);

        int id = reader.read();

        reader.back(1);

        return SessionFactor.create(id,
                                    reader
        );
    }
}
