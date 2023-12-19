package com.github.cao.awa.kalmia.user.password;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.convert.BytesValueConvertable;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class UserPassword implements BytesValueConvertable {
    private final boolean isSha;
    private final byte[] password;

    public UserPassword(boolean isSha, byte[] password) {
        this.isSha = isSha;
        if (password.length > 127) {
            throw new IllegalArgumentException("The password details cannot larger than 127 bytes");
        }
        this.password = password;
    }

    public UserPassword(String password) {
        this(true,
             Mathematics.toBytes(MessageDigger.digest(password,
                                                      MessageDigger.Sha3.SHA_512
                                 ),
                                 16
             )
        );
    }

    public boolean isSha() {
        return this.isSha;
    }

    public byte[] password() {
        return this.password;
    }

    public static UserPassword create(BytesReader reader) {
        boolean isSha = reader.read() != -1;

        int length = reader.read();
        byte[] password = reader.read(length);

        return new UserPassword(isSha, password);
    }

    @Override
    public byte[] bytes() {
        return BytesUtil.concat(new byte[]{(byte) (this.isSha ? 0 : - 1)},
                                new byte[]{(byte) this.password.length},
                                this.password
        );
    }
}
