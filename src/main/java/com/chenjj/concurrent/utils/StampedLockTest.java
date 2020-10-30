package com.chenjj.concurrent.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 使用StampedLock替代ReentrantLock
 * 在ReentrantLock锁中不存在读写分离锁，因此下面代码示例中的读写方法都是使用lock.writeLock()方法进行锁的获取，
 * 该方法会返回一个数据戳，在稍后的锁释放过程中需要用到该数据戳（stamp）
 */
public class StampedLockTest {
    private static int shareData = 0;
    private static final StampedLock lock = new StampedLock();

    public static void main(String[] args) {
        new Thread(() -> {
            inc();
        }, "write").start();

        new Thread(() -> {
            get();
        }, "read").start();
    }

    public static void inc() {
        long stamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread() + " acquired the lock.");
            // 修改共享数据
            shareData++;
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlockWrite(stamp);
        }
    }

    public static int get() {
        long stamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread() + " acquired the lock.");
            TimeUnit.SECONDS.sleep(5);
            return shareData;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
