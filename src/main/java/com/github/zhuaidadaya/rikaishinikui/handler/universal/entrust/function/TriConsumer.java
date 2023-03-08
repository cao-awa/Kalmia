package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function;

import com.github.cao.awa.apricot.anntations.*;

import java.io.Serializable;
import java.util.Objects;

@Stable
public interface TriConsumer<X, Y, Z> extends Serializable {
    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after
     *         the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     *
     */
    default TriConsumer<X, Y, Z> andThen(TriConsumer<X, Y, Z> after) {
        Objects.requireNonNull(after);
        return (x, y, z) -> {
            accept(x, y, z);
            after.accept(x, y, z);
        };
    }

    void accept(X x, Y y, Z z);
}
