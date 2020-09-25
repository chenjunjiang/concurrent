package com.chenjj.concurrent.reference;

import java.util.concurrent.TimeUnit;

/**
 * -Xmx128M -Xms64M -XX:+PrintGCDetails
 * 下面的代码无论运行多久都不会出现内存溢出，GC的时候如果发现内存不足会回收软引用关联的对象，但当内存充足的时候GC，并不会回收软引用关联的对象。
 * 所以，如果cache中插入的速度太快，GC线程没有来得及回收对象，很可能会引起内存溢出。
 */
public class SoftLRUCacheTest {
    public static void main(String[] args) throws InterruptedException {
        SoftLRUCache<Integer, Reference> cache = new SoftLRUCache<>(1000, key -> new Reference());
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            cache.get(i);
            TimeUnit.SECONDS.sleep(1);
            System.out.println("The " + i + " reference stored at cache.");
        }
    }
}
