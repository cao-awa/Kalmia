package com.github.cao.awa.apricot.util.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ApricotCollectionFactor {
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
        //        return new Object2ObjectOpenHashMap<>();
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<K, V> map) {
        return new HashMap<>(map);
        //        return new Object2ObjectOpenHashMap<>(map);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    public static <V> ArrayList<V> newArrayList() {
        return new ArrayList<>();
        //        return new ObjectArrayList<>();
    }

    public static <V> ArrayList<V> newArrayList(int capacity) {
        return new ArrayList<>(capacity);
        //        return new ObjectArrayList<>(capacity);
    }

    public static <V> LinkedList<V> newLinkedList() {
        return new LinkedList<>();
    }

    public static <V> HashSet<V> newHashSet() {
        return new HashSet<>();
        //         return new ObjectOpenHashSet<>();
    }
}
