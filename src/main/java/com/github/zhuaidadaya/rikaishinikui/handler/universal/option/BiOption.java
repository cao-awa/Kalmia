package com.github.zhuaidadaya.rikaishinikui.handler.universal.option;

import com.github.cao.awa.apricot.anntation.Stable;

/**
 * Choice one or two target.
 *
 * @param <T>
 *         Option type
 * @param first
 *         First option
 * @param second
 *         Second option
 * @author cao_awa
 * @since 1.0.0
 */
@Stable
public record BiOption<T>(T first, T second) {
    public static <X> BiOption<X> of(X x1, X x2) {
        return new BiOption<>(
                x1,
                x2
        );
    }
}
