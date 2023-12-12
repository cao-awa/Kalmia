package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.setting.Settings;
import com.github.cao.awa.kalmia.user.password.UserPassword;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class DefaultUser extends User {
    private static final byte[] HEADER = new byte[]{0};
    private final long joinTimestamp;
    private final UserPassword password;
    private final Settings settings;

    public UserPassword password() {
        return this.password;
    }

    public DefaultUser(long timestamp, String password, Settings settings) {
        this.joinTimestamp = timestamp;
        this.password = new UserPassword(password.getBytes(StandardCharsets.UTF_8));
        this.settings = settings;
    }

    public DefaultUser(long timestamp, UserPassword password, Settings settings) {
        this.joinTimestamp = timestamp;
        this.password = password;
        this.settings = settings;
    }

    public long joinTimestamp() {
        return this.joinTimestamp;
    }

    public static DefaultUser create(BytesReader reader) {
        if (reader.read() == 0) {
            long timestamp = SkippedBase256.readLong(reader);
            UserPassword password = UserPassword.create(reader);
            Settings settings = Settings.create(reader);

            return new DefaultUser(timestamp,
                                   password,
                                   settings
            );
        } else {
            return null;
        }
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(
                header(),
                SkippedBase256.longToBuf(this.joinTimestamp),
                password().toBytes(),
                settings().toBytes()
        );
    }

    @Override
    public byte[] header() {
        return HEADER;
    }

    @Override
    public Settings settings() {
        return this.settings;
    }
}
