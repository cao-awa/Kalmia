package com.github.cao.awa.kalmia.mathematic;

import java.util.Arrays;
import java.util.Comparator;

public class Mathematics {
    @SafeVarargs
    public static <T extends Number> T streamMax(T... targets) {
        return Arrays.stream(targets)
                     .max(Comparator.comparingDouble(Number :: doubleValue))
                     .orElseGet(() -> forMax(targets));
    }

    @SafeVarargs
    public static <T extends Number> T forMax(T... targets) {
        T max = targets[0];
        for (T t : targets) {
            if (t.doubleValue() > max.doubleValue()) {
                max = t;
            }
        }
        return max;
    }

//    public static <T extends Number> T max(T t1, T t2, T t3) {
//        T middle = t1.doubleValue() > t2.doubleValue() ? t1 : t2;
//        T max = middle.doubleValue() > t3.doubleValue() ? middle : t3;
//        return max;
//    }
}
