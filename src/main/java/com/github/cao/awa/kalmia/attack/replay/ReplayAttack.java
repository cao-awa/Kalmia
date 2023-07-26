package com.github.cao.awa.kalmia.attack.replay;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.time.TimeUtil;

import java.util.List;

public class ReplayAttack {
    private static final List<byte[]> invalidMarks = ApricotCollectionFactor.timedList(4000);

    public static boolean validate(byte[] mark, long timestamp) {
        if (TimeUtil.processNano(timestamp) / 1000000 > 3000) {
            return false;
        }
        boolean contains = invalidMarks.contains(mark);
        if (contains) {
            return false;
        } else {
            invalidMarks.add(mark);
            return true;
        }
    }
}
