package com.github.cao.awa.kalmia.framework.serialize.bytes;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;

@Auto
public interface BytesSerializer<T> {
    @Auto
    byte[] serialize(T target);

    @Auto
    T deserialize(BytesReader reader);

    default Class<T>[] target() {
        return Manipulate.cast(KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.target(this));
    }

    default long id() {
        return KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.id(this);
    }
}
