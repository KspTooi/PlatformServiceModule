package com.ksptooi.psm.shell;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    public V get(Object key) {
        return super.getOrDefault(key, null);
    }

    public V put(K key, V value) {
        super.put(key, value);
        return value;
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        System.out.println(cache); // {1=one, 2=two, 3=three}
        cache.get(1);
        cache.put(4, "four");
        System.out.println(cache); // {2=two, 3=three, 1=one, 4=four}
        cache.put(5, "five");
        System.out.println(cache); // {3=three, 1=one, 4=four, 5=five}
    }

}
