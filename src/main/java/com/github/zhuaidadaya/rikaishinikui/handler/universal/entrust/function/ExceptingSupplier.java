package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function;

import com.github.cao.awa.apricot.annotation.Stable;

import java.io.Serializable;

/**
 * Represents a supplier of results.
 *
 * Ref from {@link java.util.function.Supplier}, throwing Exceptions
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 */
@Stable
@FunctionalInterface
public interface ExceptingSupplier<T> extends Serializable {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Exception;
}
