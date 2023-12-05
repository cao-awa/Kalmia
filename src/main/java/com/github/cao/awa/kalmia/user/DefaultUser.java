package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.user.password.UserPassword;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class DefaultUser extends User {
    private final long joinTimestamp;
    private final UserPassword password;

    public UserPassword password() {
        return this.password;
    }

    public DefaultUser(long timestamp, String password) {
        this.joinTimestamp = timestamp;
        this.password = new UserPassword(password.getBytes(StandardCharsets.UTF_8));
    }

    public DefaultUser(long timestamp, byte[] password) {
        this.joinTimestamp = timestamp;
        this.password = new UserPassword(password);
    }

    public DefaultUser(long timestamp, UserPassword password) {
        this.joinTimestamp = timestamp;
        this.password = password;
    }

    public long joinTimestamp() {
        return this.joinTimestamp;
    }

    public static DefaultUser create(BytesReader reader) {
        if (reader.read() == 0) {
            long timestamp = SkippedBase256.readLong(reader);
            UserPassword password = UserPassword.create(reader);

            return new DefaultUser(timestamp,
                                   password
            );
        } else {
            return null;
        }
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{0},
                                SkippedBase256.longToBuf(this.joinTimestamp),
                                this.password.toBytes()
        );
    }
}
