package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.setting.Settings;
import com.github.cao.awa.kalmia.user.factor.UserFactor;

public abstract class User {
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
