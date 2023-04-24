package com.github.cao.awa.kalmia.information.compressor.deflater;

import com.github.cao.awa.apricot.anntation.Stable;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.information.compressor.InformationCompressor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@Stable
public class DeflaterCompressor implements InformationCompressor {
    public static final DeflaterCompressor INSTANCE = new DeflaterCompressor();

    /**
     * Compress using deflater with best compression
     *
     * @param bytes Data source
     * @return Compress result
     */
    public byte[] compress(byte[] bytes) {
        if (bytes.length == 0) {
            return EMPTY_BYTES;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IOUtil.write(
                    new DeflaterOutputStream(
                            out,
                            new Deflater(Deflater.BEST_COMPRESSION)
                    ),
                    bytes
            );
            return out.toByteArray();
        } catch (Exception e) {
            return bytes;
        }
    }

    /**
     * Decompress using inflater
     *
     * @param bytes Data source
     * @return Decompress result
     */
    public byte[] decompress(byte[] bytes) {
        if (bytes.length == 0) {
            return EMPTY_BYTES;
        }
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(bytes));
            IOUtil.write(
                    result,
                    inflater
            );
            return result.toByteArray();
        } catch (Exception ex) {
            return bytes;
        }
    }
}
