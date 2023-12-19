package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.setting.Settings;
import com.github.cao.awa.kalmia.user.factor.UserFactor;

public abstract class User {
    private final LongAndExtraIdentity identity;

    public User() {
        this.identity = LongAndExtraIdentity.create(TimeUtil.millions(),
                                                    BytesRandomIdentifier.create(16)
        );
    }

    public User(LongAndExtraIdentity identity) {
        this.identity = identity;
    }

    public final LongAndExtraIdentity identity() {
        return this.identity;
    }

    public abstract byte[] toBytes();

    public static User create(byte[] data) {
        BytesReader reader = BytesReader.of(data);

        return UserFactor.create(
                reader.read(),
                reader.back(1)
        );
    }

    public abstract byte[] header();

    public abstract Settings settings();
}
