package com.github.cao.awa.kalmia.debug.dependency.circular;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.List;
import java.util.function.Consumer;

public class RequiredDependency {
    private final List<Object> required = ApricotCollectionFactor.arrayList();

    public RequiredDependency add(Object dependency) {
        this.required.add(dependency);
        return this;
    }

    public List<Object> get() {
        return this.required;
    }

    public void forEach(Consumer<Object> consumer) {
        this.required.forEach(consumer);
    }
}
