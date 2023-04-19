package com.github.zhuaidadaya.rikaishinikui.handler.universal.pair;

import com.github.cao.awa.apricot.anntation.Stable;

@Stable
public record Pair<T, Y>(T left, Y right) {
    public static <T, Y> Pair<T, Y> of(T left, Y right) {
        return new Pair<>(
                left,
                right
        );
    }
}
