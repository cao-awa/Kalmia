package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.user.factor.UserFactor;

public abstract class User {
    public abstract byte[] toBytes();

    public static User create(byte[] data) {
        BytesReader reader = new BytesReader(data);

        int id = reader.read();

        reader.back(1);

        return UserFactor.create(id,
                                 reader
        );
    }
}
