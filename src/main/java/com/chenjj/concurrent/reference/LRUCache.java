package com.chenjj.concurrent.reference;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache<K, V> {
    // 记录key的顺序
    private final LinkedList<K> keyList = new LinkedList<>();
    // 存放数据
    private final Map<K, V> cache = new HashMap<>();
    // cache的最大容量
    private final int capacity;
    private final CacheLoader<K, V> cacheLoader;

    public LRUCache(int capacity, CacheLoader<K, V> cacheLoader) {
        this.capacity = capacity;
        this.cacheLoader = cacheLoader;
    }

    public void put(K key, V value) {
        // 缓存的key的数量超过容量，将最老的数据清除
        if (keyList.size() >= capacity) {
            K oldestKey = keyList.removeFirst();
            cache.remove(oldestKey);
        }
        // 如果数据已经存在，则从key列表中删除
        if (keyList.contains(key)) {
            keyList.remove(key);
        }
        // 将key放入列表最后
        keyList.addLast(key);
        cache.put(key, value);
    }

    public V get(K key) {
        V value;
        // 先将key从keyList中删除
        boolean success = keyList.remove(key);
        // 删除失败表示数据不存在
        if (!success) {
            // 加载数据到缓存
            value = cacheLoader.load(key);
            this.put(key, value);
        } else {
            // 如果删除成功，则从cache中返回数据，然后再把key放到列表最后
            value = cache.get(key);
            keyList.addLast(key);
        }
        return value;
    }

    @Override
    public String toString() {
        return "LRUCache{" +
                "keyList=" + keyList +
                '}';
    }
}
