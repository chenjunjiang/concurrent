package com.chenjj.concurrent.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 使用StampedLock替代ReentrantReadWriteLock
 * 与ReentrantReadWriteLock锁一样，StampedLock也提供了读锁和写锁这两种模式，因此StampedLock天生就支持读写分离锁的使用方式
 */
public class StampedLockTest2 {
    private static int shareData = 0;
    private static final StampedLock lock = new StampedLock();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            inc();
        }, "write").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            get();
        }, "read").start();
    }

    public static void inc() {
        // 获取写锁
        long stamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread() + " acquired the lock.");
            // 修改共享数据
            shareData++;
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放写锁
            lock.unlockWrite(stamp);
        }
    }

    public static int get() {
        // 获取读锁
        long stamp = lock.readLock();
        try {
            System.out.println(Thread.currentThread() + " acquired the lock.");
            TimeUnit.SECONDS.sleep(5);
            return shareData;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        } finally {
            // 释放读锁
            lock.unlockRead(stamp);
        }
    }
}
