package com.github.cao.awa.kalmia.network.encode.compress;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.information.compressor.InformationCompressor;
import com.github.cao.awa.kalmia.information.compressor.deflate.DeflateCompressor;
import com.github.cao.awa.kalmia.information.compressor.inaction.InactionCompressor;
import com.github.cao.awa.kalmia.information.compressor.lz4.Lz4Compressor;

import java.util.Map;

public enum RequestCompressorType {
    NONE(0,
         InactionCompressor.INSTANCE
    ), LZ4(1,
           Lz4Compressor.INSTANCE
    ), DEFLATE(2,
               DeflateCompressor.INSTANCE
    );

    public static final Map<Integer, RequestCompressorType> TYPES = ApricotCollectionFactor.newHashMap();
    private final int id;
    private final InformationCompressor compressor;

    static {
        TYPES.put(NONE.id,
                  NONE
        );
        TYPES.put(LZ4.id,
                  LZ4
        );
        TYPES.put(DEFLATE.id,
                  DEFLATE
        );
    }

    RequestCompressorType(int id, InformationCompressor compressor) {
        this.id = id;
        this.compressor = compressor;
    }

    public InformationCompressor getCompressor() {
        return this.compressor;
    }

    public int id() {
        return this.id;
    }
}
