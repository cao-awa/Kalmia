package com.github.cao.awa.kalmia.collection.timed;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimedList<T> implements List<T> {
    private final long removeTime;
    private final ScheduledExecutorService service = new ScheduledThreadPoolExecutor(2);
    private final List<T> delegate;

    public TimedList(long removeTime) {
        this.delegate = ApricotCollectionFactor.newArrayList();
        this.removeTime = removeTime;
    }

    public TimedList(List<T> delegate, long removeTime) {
        this.delegate = Collections.synchronizedList(delegate);
        this.removeTime = removeTime;
    }


    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.delegate.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.delegate.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return this.delegate.toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return this.delegate.toArray(a);
    }

    @Override
    public boolean add(T t) {
        this.delegate.add(t);
        this.service.schedule(() -> remove(t),
                              this.removeTime,
                              TimeUnit.MILLISECONDS
        );
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return this.delegate.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.delegate.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        c.forEach(this :: add);
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public T get(int index) {
        return this.delegate.get(index);
    }

    @Override
    public T set(int index, T element) {
        return this.delegate.set(index,
                                 element
        );
    }

    @Override
    public void add(int index, T element) {
        this.delegate.add(index,
                          element
        );
    }

    @Override
    public T remove(int index) {
        return this.delegate.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.delegate.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return this.delegate.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return this.delegate.listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new TimedList<>(this.delegate.subList(fromIndex,
                                                     toIndex
        ),
                               this.removeTime
        );
    }

    @Override
    public String toString() {
        return this.delegate.toString();
    }
}
