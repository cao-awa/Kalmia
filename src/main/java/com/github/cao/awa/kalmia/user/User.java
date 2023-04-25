package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;

public abstract class User {
    public abstract byte[] toBytes();

    public static User create(byte[] data) {
        BytesReader reader = new BytesReader(data);

        int id = reader.read();

        reader.back(1);

        return switch (id) {
            case - 1 -> DeletedUser.create(reader);
            case 0 -> DefaultUser.create(reader);
            case 1 -> DisabledUser.create(reader);
            default -> null;
        };
    }
}
