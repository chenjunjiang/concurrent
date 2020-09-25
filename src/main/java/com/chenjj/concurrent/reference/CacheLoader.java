package com.chenjj.concurrent.reference;

public interface CacheLoader<K, V> {
    // 当缓存中的数据不存在的时候从哪加载数据
    V load(K k);
}
