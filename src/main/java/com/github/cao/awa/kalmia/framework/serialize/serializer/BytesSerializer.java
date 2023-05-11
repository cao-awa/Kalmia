package com.github.cao.awa.kalmia.framework.serialize.serializer;

import com.github.cao.awa.apricot.anntation.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;

@Auto
public interface BytesSerializer<T> {
    @Auto
    byte[] serialize(T t) throws Exception;

    @Auto
    T deserialize(BytesReader reader) throws Exception;

    T initializer();

    long id();
}
