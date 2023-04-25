package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class DisabledUser extends User {
    private final DefaultUser source;

    public DisabledUser(DefaultUser source) {
        this.source = source;
    }

    public static DisabledUser create(BytesReader reader) {
        if (reader.read() == 1) {
            return new DisabledUser(DefaultUser.create(reader));
        } else {
            return null;
        }
    }

    public DefaultUser getSource() {
        return this.source;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{1},
                                this.source.toBytes()
        );
    }
}
