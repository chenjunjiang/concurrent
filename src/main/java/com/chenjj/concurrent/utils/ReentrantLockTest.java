package com.chenjj.concurrent.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 从下面的代码中，很明显可以看到lock被重入（多次获取），每一次的重入都会在hold计数器原有的数量基础之上加一，
 * 显式锁lock需要程序员手动控制对锁的释放操作。lock被第二次获取之后只进行了一次unlock操作，
 * 这就导致当前线程对该锁的hold数量仍旧是非0，因此并未完成对该锁的释放行为，进而导致其他线程无法获取该锁处于阻塞状态，
 * 若程序出现这样的情况则是非常危险的，因为匿名线程在生命周期结束之后，线程本身的对象引用还被AQS的exclusiveOwnerThread所持有，
 * 但是线程本身已经死亡，这样一来就没有任何线程能够对当前锁进行释放（详见lock锁的释放源码逻辑）操作了，更谈不上获取了。
 * 线程dump后发现，main线程一直在waiting：
 * "main" #1 prio=5 os_prio=0 tid=0x0000000002652800 nid=0x1938 waiting on condition [0x000000000254f000]
 * java.lang.Thread.State: WAITING (parking)
 * at sun.misc.Unsafe.park(Native Method)
 * - parking to wait for  <0x00000000d6fd7860> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
 * at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
 * at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
 * at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
 * at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
 * at com.chenjj.concurrent.utils.ReentrantLockTest.main(ReentrantLockTest.java:37)
 */
public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        final ReentrantLock lock = new ReentrantLock();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread() + " acquired the lock.");
                // 首次获取lock， hold计数器为1
                assert lock.getHoldCount() == 1;
                // 重入
                lock.lock();
                System.out.println(Thread.currentThread() + " acquired the lock again.");
                assert lock.getHoldCount() == 2;
            } finally {
                // 释放lock，但是对应的hold计数器只减1
                lock.unlock();
                // 因此当前线程还持有锁
                assert lock.getHoldCount() == 1;
            }
        }).start();

        // 休眠2s，确保上面的线程能够启动并获取锁
        TimeUnit.SECONDS.sleep(2);
        // 阻塞，永远不会获取到锁
        lock.lock();
        System.out.println("main thread acquired the lock.");
        lock.unlock();
        System.out.println("main thread release the lock.");
    }
}
