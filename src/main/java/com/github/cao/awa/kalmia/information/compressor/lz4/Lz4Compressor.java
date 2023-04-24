package com.github.cao.awa.kalmia.information.compressor.lz4;

import com.github.cao.awa.apricot.anntation.Stable;
import com.github.cao.awa.kalmia.information.compressor.InformationCompressor;
import net.jpountz.lz4.LZ4Factory;

@Stable
public class Lz4Compressor implements InformationCompressor {
    public static final Lz4Compressor INSTANCE = new Lz4Compressor();

    /**
     * Compress using lz4 with the fastest compression
     *
     * @param bytes Data source
     * @return Compress result
     */
    public byte[] compress(byte[] bytes) {
        return LZ4Factory.fastestInstance()
                         .fastCompressor()
                         .compress(bytes);
    }

    /**
     * Decompress using lz4
     *
     * @param bytes Data source
     * @return Decompress result
     */
    public byte[] decompress(byte[] bytes) {
        return LZ4Factory.fastestInstance()
                         .fastDecompressor()
                         .decompress(
                                 bytes,
                                 bytes.length
                         );
    }
}