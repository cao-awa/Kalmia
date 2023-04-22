package com.github.cao.awa.kalmia.protocol;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class RequestProtocol {
    private final String name;
    private final long version;

    public RequestProtocol(String name, long version) {
        this.name = name;
        this.version = version;
    }

    public String name() {
        return this.name;
    }

    public long version() {
        return this.version;
    }

    public static RequestProtocol create(BytesReader reader) {
        return new RequestProtocol(new String(reader.read(reader.read()),
                                              StandardCharsets.US_ASCII
        ),
                                   SkippedBase256.readLong(reader)
        );
    }

    public byte[] toBytes() {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            output.write(this.name.length());
            output.write(this.name.getBytes(StandardCharsets.US_ASCII));
            output.write(SkippedBase256.longToBuf(this.version));
            return output.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
}
