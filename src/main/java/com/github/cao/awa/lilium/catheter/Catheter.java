package com.github.cao.awa.lilium.catheter;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Catheter<T> {
    private static final Random RANDOM = new Random();
    private T[] targets;

    public Catheter(T[] targets) {
        this.targets = targets;
    }

    @SafeVarargs
    public static <X> Catheter<X> make(X... targets) {
        return new Catheter<>(targets);
    }

    public static <X> Catheter<X> of(X[] targets) {
        return new Catheter<>(targets);
    }

    @SuppressWarnings("unchecked")
    public static <X> Catheter<X> of(Set<X> targets) {
        return new Catheter<>((X[]) targets.toArray(Object[] :: new));
    }

    @SuppressWarnings("unchecked")
    public static <X> Catheter<X> of(List<X> targets) {
        return new Catheter<>((X[]) targets.toArray(Object[] :: new));
    }

    public Catheter<T> each(Consumer<T> action) {
        T[] ts = this.targets;
        int index = 0, length = ts.length;
        while (index < length) {
            action.accept(ts[index++]);
        }
        return this;
    }

    public Catheter<T> filter(Predicate<T> predicate) {
        T[] ts = this.targets;
        int tsLength = ts.length;
        int newDelegateSize = tsLength;

        int index = 0;
        while (index < tsLength) {
            T target = ts[index];
            if (predicate.test(target)) {
                index++;
                continue;
            }
            ts[index++] = null;
            newDelegateSize--;
        }

        T[] newDelegate = array(newDelegateSize);
        int newDelegateIndex = 0;
        index = 0;
        while (index < tsLength) {
            T t = ts[index++];
            if (t == null) {
                continue;
            }
            newDelegate[newDelegateIndex++] = t;
        }
        this.targets = newDelegate;
        return this;
    }

    public Catheter<T> distinct() {
        Map<T, Boolean> map = ApricotCollectionFactor.hashMap();
        return filter(
                item -> {
                    if (map.getOrDefault(item,
                                         false
                    )) {
                        return false;
                    }
                    map.put(item,
                            true
                    );
                    return true;
                }
        );
    }

    public Catheter<T> sort() {
        Arrays.sort(this.targets);
        return this;
    }

    public Catheter<T> sort(Comparator<T> comparator) {
        Arrays.sort(this.targets,
                    comparator
        );
        return this;
    }

    public Catheter<T> holdTill(int index) {
        index = Math.min(index,
                         this.targets.length
        );

        T[] ts = this.targets;
        T[] newDelegate = array(index);
        if (index > 0) {
            System.arraycopy(
                    ts,
                    0,
                    newDelegate,
                    0,
                    index
            );
        }
        this.targets = newDelegate;

        return this;
    }

    public Catheter<T> holdTill(Predicate<T> predicate) {
        int index = findTill(predicate);

        T[] ts = this.targets;
        T[] newDelegate = array(index);
        if (index > 0) {
            System.arraycopy(
                    ts,
                    0,
                    newDelegate,
                    0,
                    index
            );
        }
        this.targets = newDelegate;

        return this;
    }

    public T flock(T source, BiFunction<T, T, T> maker) {
        T result = source;
        T[] ts = this.targets;
        int index = 0, tsLength = ts.length;
        while (index < tsLength) {
            result = maker.apply(result,
                                 ts[index++]
            );
        }
        return result;
    }

    @Nullable
    public T flock(BiFunction<T, T, T> maker) {
        T[] ts = this.targets;
        T result = ts.length > 0 ? ts[0] : null;
        if (result != null) {
            for (int i = 1, length = ts.length; i < length; i++) {
                result = maker.apply(result,
                                     ts[i]
                );
            }
        }
        return result;
    }

    public Catheter<T> waiveTill(int index) {
        T[] ts = this.targets;
        T[] newDelegate;
        if (index >= ts.length) {
            newDelegate = array(0);
        } else {
            newDelegate = array(ts.length - index + 1);
            System.arraycopy(
                    ts,
                    index - 1,
                    newDelegate,
                    0,
                    newDelegate.length
            );
        }
        this.targets = newDelegate;

        return this;
    }

    public Catheter<T> waiveTill(Predicate<T> predicate) {
        int index = findTill(predicate);

        T[] ts = this.targets;
        T[] newDelegate;
        if (index >= ts.length) {
            newDelegate = array(0);
        } else {
            newDelegate = array(ts.length - index + 1);
            System.arraycopy(
                    ts,
                    index - 1,
                    newDelegate,
                    0,
                    newDelegate.length
            );
        }
        this.targets = newDelegate;

        return this;
    }

    public Catheter<T> till(Predicate<T> predicate) {
        T[] ts = this.targets;
        int index = 0, length = ts.length;
        while (index < length) {
            if (predicate.test(ts[index++])) {
                break;
            }
        }

        return this;
    }

    public int findTill(Predicate<T> predicate) {
        T[] ts = this.targets;
        int index = 0, length = ts.length;
        while (index < length) {
            if (predicate.test(ts[index++])) {
                break;
            }
        }

        return index;
    }

    public Catheter<T> replace(Function<T, T> handler) {
        T[] ts = this.targets;
        int index = 0, length = ts.length;
        while (index < length) {
            ts[index] = handler.apply(ts[index++]);
        }
        return this;
    }

    public <X> Catheter<X> vary(Function<T, X> handler) {
        T[] ts = this.targets;
        X[] array = array(ts.length);
        int index = 0, length = ts.length;
        while (index < length) {
            array[index] = handler.apply(ts[index++]);
        }
        return new Catheter<>(array);
    }

    public Catheter<T> whenAny(Predicate<T> predicate, Consumer<T> action) {
        T[] ts = this.targets;
        int index = 0, tsLength = ts.length;
        while (index < tsLength) {
            T t = ts[index++];
            if (predicate.test(t)) {
                action.accept(t);
                break;
            }
        }
        return this;
    }

    public Catheter<T> whenAll(Predicate<? super T> predicate, Runnable action) {
        T[] ts = this.targets;
        int index = 0, tsLength = ts.length;
        while (index < tsLength) {
            T t = ts[index++];
            if (! predicate.test(t)) {
                return this;
            }
        }
        action.run();
        return this;
    }

    public Catheter<T> whenAll(Predicate<? super T> predicate, Consumer<T> action) {
        return whenAll(predicate,
                       () -> each(action)
        );
    }

    private Catheter<T> whenNone(Predicate<? super T> predicate, Runnable action) {
        T[] ts = this.targets;
        int index = 0, tsLength = ts.length;
        while (index < tsLength) {
            T t = ts[index++];
            if (predicate.test(t)) {
                return this;
            }
        }
        action.run();
        return this;
    }

    public boolean hasAny(Predicate<? super T> predicate) {
        T[] ts = this.targets;
        int index = 0, tsLength = ts.length;
        while (index < tsLength) {
            if (predicate.test(ts[index++])) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAll(Predicate<? super T> predicate) {
        T[] ts = this.targets;
        int index = 0, tsLength = ts.length;
        while (index < tsLength) {
            if (! predicate.test(ts[index++])) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNone(Predicate<? super T> predicate) {
        T[] ts = this.targets;
        int index = 0, tsLength = ts.length;
        while (index < tsLength) {
            if (predicate.test(ts[index++])) {
                return false;
            }
        }
        return true;
    }

    public Catheter<T> any(Consumer<T> consumer) {
        if (this.targets.length > 0) {
            consumer.accept(EntrustEnvironment.select(this.targets,
                                                      RANDOM
            ));
        }
        return this;
    }

    public Catheter<T> first(Consumer<T> consumer) {
        if (this.targets.length > 0) {
            consumer.accept(this.targets[0]);
        }
        return this;
    }

    public Catheter<T> tail(Consumer<T> consumer) {
        if (this.targets.length > 0) {
            consumer.accept(this.targets[this.targets.length - 1]);
        }
        return this;
    }

    public Catheter<T> reverse() {
        T[] ts = this.targets;
        int index = 0, length = ts.length, split = length / 2;
        T temp;
        for (; index < split; index++) {
            int swapIndex = length - index - 1;
            temp = ts[index];
            ts[index] = ts[swapIndex];
            ts[swapIndex] = temp;
        }
        return this;
    }

    public T max(Comparator<? super T> comparator) {
        return flock((result, element) -> comparator.compare(result,
                                                             element
        ) < 0 ? element : result);
    }

    public T min(Comparator<? super T> comparator) {
        return flock((result, element) -> comparator.compare(result,
                                                             element
        ) > 0 ? element : result);
    }

    public Optional<T> selectMax(Comparator<? super T> comparator) {
        return Optional.ofNullable(flock((result, element) -> comparator.compare(result,
                                                                                 element
        ) < 0 ? element : result));
    }

    public Optional<T> selectMin(Comparator<? super T> comparator) {
        return Optional.ofNullable(flock((result, element) -> comparator.compare(result,
                                                                                 element
        ) > 0 ? element : result));
    }

    public Catheter<T> whenMax(Comparator<? super T> comparator, Consumer<T> action) {
        T t = flock((result, element) -> comparator.compare(result,
                                                            element
        ) < 0 ? element : result);
        if (t != null) {
            action.accept(t);
        }
        return this;
    }

    public Catheter<T> whenMin(Comparator<? super T> comparator, Consumer<T> action) {
        T t = flock((result, element) -> comparator.compare(result,
                                                            element
        ) > 0 ? element : result);
        if (t != null) {
            action.accept(t);
        }
        return this;
    }

    public int count() {
        return this.targets.length;
    }

    public T[] array() {
        return this.targets;
    }

    public List<T> list() {
        return ApricotCollectionFactor.arrayList(this.targets);
    }

    public Set<T> set() {
        return ApricotCollectionFactor.hashSet(this.targets);
    }

    @SuppressWarnings("unchecked")
    private static <X> X[] array(int size) {
        return (X[]) new Object[size];
    }
}
