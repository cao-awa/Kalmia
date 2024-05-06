package com.github.cao.awa.lilium.catheter;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;


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

    public Catheter<T> each(final Consumer<T> action) {
        final T[] ts = this.targets;
        int index = 0;
        final int length = ts.length;
        while (index < length) {
            action.accept(ts[index++]);
        }
        return this;
    }

    /**
     * Holding items that matched given predicate.
     *
     * @param predicate The filter predicate
     * @return This {@code Catheter<T>}
     * @author 草
     * @since 1.0.0
     */
    public Catheter<T> filter(final Predicate<T> predicate) {
        // 创建需要的变量和常量
        final T[] ts = this.targets;
        final int length = ts.length;
        int newDelegateSize = length;
        int index = 0;

        // 遍历所有元素
        while (index < length) {
            T target = ts[index];

            // 符合条件的保留
            if (predicate.test(target)) {
                index++;
                continue;
            }

            // 不符合条件的设为null，后面会去掉
            // 并且将新数组的容量减一
            ts[index++] = null;
            newDelegateSize--;
        }

        // 创建新数组
        final T[] newDelegate = array(newDelegateSize);
        int newDelegateIndex = 0;
        index = 0;

        // 遍历所有元素
        while (index < length) {
            final T t = ts[index++];

            // 为null则为被筛选掉的，忽略
            if (t == null) {
                continue;
            }

            // 不为null则加入新数组
            newDelegate[newDelegateIndex++] = t;
        }

        // 替换当前数组，不要创建新Catheter对象以节省性能
        this.targets = newDelegate;

        return this;
    }

    /**
     * Holding items that matched given predicate.
     *
     * @param initializer A constant to passed to next parameter
     * @param predicate The filter predicate
     * @return This {@code Catheter<T>}
     * @param <X> Initializer constant
     *
     * @author 草二号机
     *
     * @since 1.0.0
     */
    public <X> Catheter<T> filter(final X initializer, final BiPredicate<T, X> predicate) {
        return filter(item -> predicate.test(item,
                                             initializer
        ));
    }

    /**
     * Holding items that matched given predicate.
     *
     * @param succeed   Direct done filter? When succeed true, cancel filter instantly
     * @param predicate The filter predicate
     * @return This {@code Catheter<T>}
     * @author 草
     * @since 1.0.0
     */
    public Catheter<T> orFilter(final boolean succeed, final Predicate<T> predicate) {
        if (succeed) {
            return this;
        }
        return filter(predicate);
    }

    /**
     * Holding items that matched given predicate.
     *
     * @param succeed     Direct done filter? When succeed true, cancel filter instantly
     * @param initializer A constant to passed to next parameter
     * @param predicate   The filter predicate
     * @param <X>         Initializer constant
     * @return This {@code Catheter<T>}
     * @author 草
     * @author 草二号机
     * @since 1.0.0
     */
    public <X> Catheter<T> orFilter(final boolean succeed, final X initializer, final BiPredicate<T, X> predicate) {
        if (succeed) {
            return this;
        }
        return filter(initializer,
                      predicate
        );
    }

    public Catheter<T> distinct() {
        final Map<T, Boolean> map = ApricotCollectionFactor.hashMap();
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

        final T[] ts = this.targets;
        final T[] newDelegate = array(index);
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

    public Catheter<T> holdTill(final Predicate<T> predicate) {
        final int index = findTill(predicate);

        final T[] ts = this.targets;
        final T[] newDelegate = array(index);
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

    public T flock(final T source, final BiFunction<T, T, T> maker) {
        T result = source;
        final T[] ts = this.targets;
        int index = 0;
        final int length = ts.length;
        while (index < length) {
            result = maker.apply(result,
                                 ts[index++]
            );
        }
        return result;
    }

    @Nullable
    public T flock(final BiFunction<T, T, T> maker) {
        final T[] ts = this.targets;
        final int length = ts.length;
        T result = length > 0 ? ts[0] : null;
        if (result != null) {
            for (int i = 1; i < length; i++) {
                result = maker.apply(result,
                                     ts[i]
                );
            }
        }
        return result;
    }

    public Catheter<T> waiveTill(final int index) {
        final T[] ts = this.targets;
        final T[] newDelegate;
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

    public Catheter<T> waiveTill(final Predicate<T> predicate) {
        final int index = findTill(predicate);

        final T[] ts = this.targets;
        final T[] newDelegate;
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

    public Catheter<T> till(final Predicate<T> predicate) {
        final T[] ts = this.targets;
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            if (predicate.test(ts[index++])) {
                break;
            }
        }

        return this;
    }

    public int findTill(final Predicate<T> predicate) {
        final T[] ts = this.targets;
        int index = 0, length = ts.length;
        while (index < length) {
            if (predicate.test(ts[index++])) {
                break;
            }
        }

        return index;
    }

    public Catheter<T> replace(final Function<T, T> handler) {
        final T[] ts = this.targets;
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            ts[index] = handler.apply(ts[index++]);
        }
        return this;
    }

    public <X> Catheter<X> vary(final Function<T, X> handler) {
        final T[] ts = this.targets;
        final X[] array = array(ts.length);
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            array[index] = handler.apply(ts[index++]);
        }
        return new Catheter<>(array);
    }

    public Catheter<T> whenAny(final Predicate<T> predicate, final Consumer<T> action) {
        final T[] ts = this.targets;
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            final T t = ts[index++];
            if (predicate.test(t)) {
                action.accept(t);
                break;
            }
        }
        return this;
    }

    public Catheter<T> whenAll(final Predicate<T> predicate, final Runnable action) {
        final T[] ts = this.targets;
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            final T t = ts[index++];
            if (! predicate.test(t)) {
                return this;
            }
        }
        action.run();
        return this;
    }

    public Catheter<T> whenAll(final Predicate<T> predicate, final Consumer<T> action) {
        return whenAll(predicate,
                       () -> each(action)
        );
    }

    private Catheter<T> whenNone(final Predicate<T> predicate, final Runnable action) {
        final T[] ts = this.targets;
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            final T t = ts[index++];
            if (predicate.test(t)) {
                return this;
            }
        }
        action.run();
        return this;
    }

    public boolean hasAny(final Predicate<T> predicate) {
        final T[] ts = this.targets;
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            if (predicate.test(ts[index++])) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAll(final Predicate<T> predicate) {
        final T[] ts = this.targets;
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            if (! predicate.test(ts[index++])) {
                return false;
            }
        }
        return true;
    }

    public boolean hasNone(final Predicate<T> predicate) {
        final T[] ts = this.targets;
        final int length = ts.length;
        int index = 0;
        while (index < length) {
            if (predicate.test(ts[index++])) {
                return false;
            }
        }
        return true;
    }

    public Catheter<T> any(final Consumer<T> consumer) {
        if (this.targets.length > 0) {
            consumer.accept(EntrustEnvironment.select(this.targets,
                                                      RANDOM
            ));
        }
        return this;
    }

    public Catheter<T> first(final Consumer<T> consumer) {
        if (this.targets.length > 0) {
            consumer.accept(this.targets[0]);
        }
        return this;
    }

    public Catheter<T> tail(final Consumer<T> consumer) {
        if (this.targets.length > 0) {
            consumer.accept(this.targets[this.targets.length - 1]);
        }
        return this;
    }

    public Catheter<T> reverse() {
        final T[] ts = this.targets;
        final int length = ts.length;
        final int split = length / 2;
        int index = 0;
        T temp;
        for (; index < split; index++) {
            final int swapIndex = length - index - 1;
            temp = ts[index];
            ts[index] = ts[swapIndex];
            ts[swapIndex] = temp;
        }
        return this;
    }

    public T max(final Comparator<T> comparator) {
        return flock((result, element) -> comparator.compare(result,
                                                             element
        ) < 0 ? element : result);
    }

    public T min(final Comparator<T> comparator) {
        return flock((result, element) -> comparator.compare(result,
                                                             element
        ) > 0 ? element : result);
    }

    public Optional<T> selectMax(final Comparator<T> comparator) {
        return Optional.ofNullable(flock((result, element) -> comparator.compare(result,
                                                                                 element
        ) < 0 ? element : result));
    }

    public Optional<T> selectMin(final Comparator<T> comparator) {
        return Optional.ofNullable(flock((result, element) -> comparator.compare(result,
                                                                                 element
        ) > 0 ? element : result));
    }

    public Catheter<T> whenMax(final Comparator<T> comparator, final Consumer<T> action) {
        final T t = flock((result, element) -> comparator.compare(result,
                                                                  element
        ) < 0 ? element : result);
        if (t != null) {
            action.accept(t);
        }
        return this;
    }

    public Catheter<T> whenMin(final Comparator<T> comparator, final Consumer<T> action) {
        final T t = flock((result, element) -> comparator.compare(result,
                                                                  element
        ) > 0 ? element : result);
        if (t != null) {
            action.accept(t);
        }
        return this;
    }

    public Catheter<T> exists() {
        return filter(Objects :: nonNull);
    }

    public int count() {
        return this.targets.length;
    }

    public Catheter<T> count(AtomicInteger target) {
        target.set(count());
        return this;
    }

    public Catheter<T> count(Consumer<Integer> consumer) {
        consumer.accept(count());
        return this;
    }

    public Catheter<T> dump() {
        return new Catheter<>(array());
    }

    public T[] array() {
        return this.targets.clone();
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
