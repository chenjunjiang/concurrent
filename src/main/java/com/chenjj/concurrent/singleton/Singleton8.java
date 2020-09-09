package com.chenjj.concurrent.singleton;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 前面几种方式实现单例都会使用到锁来保证在多线程环境下的唯一性，有些是显示使用synchronized,有些是使用
 * <clinit>()方法，<clinit>()方法是同步方法，也会用到锁。
 * 那么有没有什么办法可以不使用synchronized和lock来实现一个线程安全的单例？
 * 可以使用CAS来实现线程安全的单例
 * 用CAS的好处在于不需要使用传统的锁机制来保证线程安全，CAS是一种基于忙等待的算法，依赖底层硬件的实现,
 * 相对于锁它没有线程切换和阻塞的额外消耗，可以支持较大的并行度。
 * CAS的一个重要缺点在于如果忙等待一直执行不成功(一直在死循环中),会对CPU造成较大的执行开销。
 * 而且，这种写法如果有多个线程同时执行singleton = new Singleton();也会比较浪费堆内存。
 */
public class Singleton8 {
    private static final AtomicReference<Singleton8> INSTANCE = new AtomicReference<>();

    private Singleton8() {
    }

    public static Singleton8 getInstance() {
        for (; ; ) {
            Singleton8 singleton8 = INSTANCE.get();
            if (singleton8 != null) {
                return singleton8;
            }
            singleton8 = new Singleton8();
            if (INSTANCE.compareAndSet(null, singleton8)) {
                return singleton8;
            }
        }
    }
}
