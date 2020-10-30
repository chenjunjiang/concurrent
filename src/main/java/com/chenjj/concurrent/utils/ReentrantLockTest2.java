package com.chenjj.concurrent.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程dump信息：
 * Found one Java-level deadlock:
 * =============================
 * "Thread-1":
 * waiting for ownable synchronizer 0x00000000d6fd6c90, (a java.util.concurrent.locks.ReentrantLock$NonfairSync),
 * which is held by "Thread-0"
 * "Thread-0":
 * waiting for ownable synchronizer 0x00000000d6fd6cc0, (a java.util.concurrent.locks.ReentrantLock$NonfairSync),
 * which is held by "Thread-1"
 * <p>
 * Java stack information for the threads listed above:
 * ===================================================
 * "Thread-1":
 * at sun.misc.Unsafe.park(Native Method)
 * - parking to wait for  <0x00000000d6fd6c90> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
 * at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
 * at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
 * at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
 * at com.chenjj.concurrent.utils.ReentrantLockTest2.m2(ReentrantLockTest2.java:46)
 * at com.chenjj.concurrent.utils.ReentrantLockTest2.lambda$main$1(ReentrantLockTest2.java:19)
 * at com.chenjj.concurrent.utils.ReentrantLockTest2$$Lambda$2/1922154895.run(Unknown Source)
 * at java.lang.Thread.run(Thread.java:748)
 * "Thread-0":
 * at sun.misc.Unsafe.park(Native Method)
 * - parking to wait for  <0x00000000d6fd6cc0> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
 * at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
 * at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
 * at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
 * at com.chenjj.concurrent.utils.ReentrantLockTest2.m1(ReentrantLockTest2.java:28)
 * at com.chenjj.concurrent.utils.ReentrantLockTest2.lambda$main$0(ReentrantLockTest2.java:13)
 * at com.chenjj.concurrent.utils.ReentrantLockTest2$$Lambda$1/793589513.run(Unknown Source)
 * at java.lang.Thread.run(Thread.java:748)
 */
public class ReentrantLockTest2 {
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    /**
     * 不断循环获取锁，某一个时刻就会出现死锁
     *
     * @param args
     */
    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                m1();
            }
        }).start();

        new Thread(() -> {
            while (true) {
                m2();
            }
        }).start();
    }

    private static void m1() {
        lock1.lock();
        System.out.println(Thread.currentThread() + " get lock1.");
        try {
            lock2.lock();
            System.out.println(Thread.currentThread() + " get lock2.");
            try {
                //
            } finally {
                lock2.unlock();
                System.out.println(Thread.currentThread() + " release lock2.");
            }
        } finally {
            lock1.unlock();
            System.out.println(Thread.currentThread() + " release lock1.");
        }
    }

    private static void m2() {
        lock2.lock();
        System.out.println(Thread.currentThread() + " get lock2.");
        try {
            lock1.lock();
            System.out.println(Thread.currentThread() + " get lock1.");
            try {
                //
            } finally {
                lock1.unlock();
                System.out.println(Thread.currentThread() + " release lock1.");
            }
        } finally {
            lock2.unlock();
            System.out.println(Thread.currentThread() + " release lock2.");
        }
    }
}
