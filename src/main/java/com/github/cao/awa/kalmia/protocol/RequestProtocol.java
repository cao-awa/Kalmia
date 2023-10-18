package com.github.cao.awa.kalmia.protocol;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializable;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * The transport protocol of kalmiagram.
 *
 * @author cao_awa
 * @author 草二号机
 * @since 1.0.0
 */
public final class RequestProtocol implements BytesSerializable<RequestProtocol> {
    private String name;
    private long version;
    private long compatible;
    private boolean forceUse;

    /**
     * @param name       Protocol name.
     * @param version    Protocol version.
     * @param compatible Compatible lowest version.
     * @param forceUse   Always use the protocol, even if incompatible.
     */
    public RequestProtocol(String name, long version, long compatible, boolean forceUse) {
        this.name = name;
        this.version = version;
        this.compatible = compatible;
        this.forceUse = forceUse;
    }

    @Auto
    public RequestProtocol() {

    }

    public static RequestProtocol create(BytesReader reader) {
        return new RequestProtocol(new String(reader.read(reader.read()),
                                              StandardCharsets.US_ASCII
        ),
                                   SkippedBase256.readLong(reader),
                                   SkippedBase256.readLong(reader),
                                   reader.read() == 1
        );
    }

    public byte[] toBytes() {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            output.write(this.name.length());
            output.write(this.name.getBytes(StandardCharsets.US_ASCII));
            output.write(SkippedBase256.longToBuf(this.version));
            output.write(SkippedBase256.longToBuf(this.compatible));
            output.write(this.forceUse ? 1 : 0);
            return output.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    public String name() {
        return name;
    }

    public long version() {
        return version;
    }

    public long compatible() {
        return compatible;
    }

    public boolean forceUse() {
        return forceUse;
    }

    @Override
    public byte[] serialize() {
        return toBytes();
    }

    @Override
    public RequestProtocol deserialize(BytesReader reader) {
        this.name = new String(reader.read(reader.read()),
                               StandardCharsets.US_ASCII
        );
        this.version = SkippedBase256.readLong(reader);
        this.compatible = SkippedBase256.readLong(reader);
        this.forceUse = reader.read() == 1;

        return this;
    }

    public boolean canUse(RequestProtocol protocol) {
        return protocol.forceUse() || protocol.compatible() <= version();
    }
}
