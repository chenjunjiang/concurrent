package com.chenjj.concurrent.reference;

import java.util.concurrent.TimeUnit;

/**
 * -Xmx128M -Xms64M -XX:+PrintGCDetails
 */
public class LRUCacheTest {
    public static void main(String[] args) throws InterruptedException {
        /*LRUCache<String, Reference> cache = new LRUCache<>(5, key -> new Reference());
        cache.get("Alex");
        cache.get("Jack");
        cache.get("Gavin");
        cache.get("Dillon");
        cache.get("Leo");
        // Alex将会被提出
        cache.get("Jenny");
        System.out.println(cache.toString());*/

        // 当不断地往cache中存放数据或者存放固定数量的数据时，由于是Strong reference的缘故，可能会引起内存溢出的情况
        LRUCache<Integer, Reference> cache = new LRUCache<>(200, key -> new Reference());
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            cache.get(i);
            TimeUnit.SECONDS.sleep(1);
            System.out.println("The " + i + " reference stored at cache.");
        }
    }
}
