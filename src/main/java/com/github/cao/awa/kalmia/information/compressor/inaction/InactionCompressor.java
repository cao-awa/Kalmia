package com.github.cao.awa.kalmia.information.compressor.inaction;

import com.github.cao.awa.kalmia.information.compressor.InformationCompressor;

public class InactionCompressor implements InformationCompressor {
    public static final InactionCompressor INSTANCE = new InactionCompressor();

    /**
     * Return the source back, because inaction compressor will not compress the data.
     *
     * @param bytes Data source
     * @return Decompress result
     */
    @Override
    public byte[] compress(byte[] bytes) {
        return bytes;
    }

    /**
     * Return the source back, because inaction compressor will not compress the data.
     *
     * @param bytes Data source
     * @return Decompress result
     */
    @Override
    public byte[] decompress(byte[] bytes) {
        return bytes;
    }
}
