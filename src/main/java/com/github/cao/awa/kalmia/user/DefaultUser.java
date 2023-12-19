package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.setting.Settings;
import com.github.cao.awa.kalmia.user.password.UserPassword;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class DefaultUser extends User {
    private static final byte[] HEADER = new byte[]{0};
    private final UserPassword password;
    private final Settings settings;

    public UserPassword password() {
        return this.password;
    }

    public DefaultUser(UserPassword password, Settings settings) {
        this.password = password;
        this.settings = settings;
    }

    public DefaultUser(LongAndExtraIdentity identity, UserPassword password, Settings settings) {
        super(identity);
        this.password = password;
        this.settings = settings;
    }

    public long joinTimestamp() {
        return identity().longValue();
    }

    public static DefaultUser create(BytesReader reader) {
        if (reader.read() == 0) {
            LongAndExtraIdentity identity = LongAndExtraIdentity.read(reader);
            UserPassword password = UserPassword.create(reader);
            Settings settings = Settings.create(reader);

            return new DefaultUser(identity,
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
                identity().toBytes(),
                password().bytes(),
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
