package com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle;

import com.github.cao.awa.apricot.annotations.Stable;

import java.util.function.Supplier;

@Stable
public final class Receptacle<T> {
    private T target;

    public Receptacle(T target) {
        this.target = target;
    }

    public static <X> Receptacle<X> of() {
        return of(null);
    }

    public static <X> Receptacle<X> of(X target) {
        return new Receptacle<>(target);
    }

    public T getOrSet(Supplier<T> target) {
        if (this.target == null) {
            return set(target.get()).get();
        }
        return get();
    }

    public T get() {
        return this.target;
    }

    public Receptacle<T> set(T target) {
        this.target = target;
        return this;
    }
}
