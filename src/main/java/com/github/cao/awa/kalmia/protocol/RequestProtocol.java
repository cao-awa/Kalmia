package com.github.cao.awa.kalmia.protocol;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * The transport protocol of kalmiagram.
 *
 * @param name       Protocol name.
 * @param version    Protocol version.
 * @param compatible Compatible lowest version.
 * @param forceUse   Always use the protocol, even if incompatible.
 * @author cao_awa
 * @author 草二号机
 * @since 1.0.0
 */
public record RequestProtocol(String name, long version, long compatible, boolean forceUse) {
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
}
