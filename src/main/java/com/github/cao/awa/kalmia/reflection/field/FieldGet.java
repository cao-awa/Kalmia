package com.github.cao.awa.kalmia.reflection.field;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;

import java.util.List;
import java.util.function.Function;

public class FieldGet<R, I> {
    private final Class<I> target;
    private final List<String> gets = ApricotCollectionFactor.arrayList();
    private final List<Function<Class<I>, R>> backups = ApricotCollectionFactor.arrayList();

    private FieldGet(I target) {
        this.target = Manipulate.cast(target.getClass());
    }

    private FieldGet(Class<I> target) {
        this.target = Manipulate.cast(target);
    }

    public static <X, Y> FieldGet<X, Y> create(Y target, String name) {
        return new FieldGet<X, Y>(target).or(name);
    }

    public static <X, Y> FieldGet<X, Y> create(Class<Y> target, String name) {
        return new FieldGet<X, Y>(target).or(name);
    }

    public FieldGet<R, I> or(String name) {
        this.gets.add(name);
        return this;
    }

    public FieldGet<R, I> or(Function<Class<I>, R> function) {
        this.backups.add(function);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <Z> Z get() {
        Class<I> clazz = Manipulate.cast(this.target);

        assert clazz != null;
        for (String name : this.gets) {
            try {
                Object o = clazz.getField(name)
                                .get(this.target);

                if (o != null) {
                    return (Z) o;
                }
            } catch (Exception e) {

            }
        }

        for (Function<Class<I>, R> supplier : this.backups) {
            try {
                R r = supplier.apply(clazz);

                if (r != null) {
                    return (Z) r;
                }
            } catch (Exception e) {

            }
        }

        return null;
    }
}
