package top.wrqj.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MemoryCache<K, V> implements Cache<K, V> {

    private final Map<K, V> cache = new ConcurrentHashMap<>();

    public void computeIfAbsent(K key, V value) {
        cache.computeIfAbsent(key, (k) -> {
            put(k, value);
            return value;
        });
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public V remove(K key) {
        return cache.remove(key);
    }
}
