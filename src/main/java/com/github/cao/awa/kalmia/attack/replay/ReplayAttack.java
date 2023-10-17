package com.github.cao.awa.kalmia.attack.replay;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.Mathematics;

import java.util.List;

public class ReplayAttack {
    //    private static final List<byte[]> invalidMarks = ApricotCollectionFactor.timedList(4000);
    private static final List<String> invalidMarks = ApricotCollectionFactor.timedList(4000);

    public static boolean validate(byte[] mark, long timestamp) {
        if (TimeUtil.processNano(timestamp) / 1000000 > 3000) {
            return false;
        }

        String radixMark = Mathematics.radix(mark,
                                             36
        );

        if (invalidMarks.contains(radixMark)) {
            return false;
        } else {
            invalidMarks.add(radixMark);
            return true;
        }
    }
}
