package com.github.cao.awa.kalmia.network.encode.crypto.symmetric.no;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class NoCrypto extends SymmetricCrypto {
    public NoCrypto() {
        super(BytesUtil.EMPTY);
    }

    @Override
    public byte[] encode(byte[] plains) throws Exception {
        return BytesUtil.concat(plains);
    }

    @Override
    public byte[] decode(byte[] ciphertext) throws Exception {
        BytesReader reader = new BytesReader(ciphertext);
        return reader.all();
    }
}
