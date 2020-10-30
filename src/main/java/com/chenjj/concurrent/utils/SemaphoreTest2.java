package com.chenjj.concurrent.utils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 无论是synchronized关键字，还是我们之前定义的若干显式锁，都存在一个问题，那就是，当某个时刻获取不到锁的时候，
 * 当前线程会进入阻塞状态。这种状态有些时候并不是我们所期望的，如果获取不到锁线程还可以进行其他的操作，
 * 而不一定非得将其阻塞（事实上，Lock接口中就提供了try lock的方法，
 * 当某个线程获取不到对共享资源执行的权限时将会立即返回，而不是使当前线程进入阻塞状态），
 * 下面将借助Semaphore提供的方法实现一个显式锁，该锁的主要作用是try锁，若获取不到锁就会立即返回。
 */
public class SemaphoreTest2 {
    public static void main(String[] args) {
        /*final Semaphore semaphore = new Semaphore(1);
        new Thread(() -> {
            try {
                semaphore.acquire();
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();

        // 并不要求release的线程就是acquire的线程，任何线程都可以release
        semaphore.release();*/

        /*final TryLock tryLock = new TryLock();
        new Thread(() -> {
            boolean gotLock = tryLock.tryLock();
            if (!gotLock) {
                System.out.println(Thread.currentThread() + " can't get the lock, will do other thing.");
                return;
            }
            try {
                simulateWork();
            } finally {
                tryLock.unlock();
            }
        }).start();

        // main线程也参与lock的竞争
        boolean gotLock = tryLock.tryLock();
        if (!gotLock) {
            System.out.println(Thread.currentThread() + " can't get the lock, will do other thing.");
            return;
        }
        try {
            simulateWork();
        } finally {
            tryLock.unlock();
        }*/
    }

    private static class TryLock {
        private final Semaphore semaphore = new Semaphore(1);

        public boolean tryLock() {
            return semaphore.tryAcquire();
        }

        public void unlock() {
            semaphore.release();
            System.out.println(Thread.currentThread() + " release lock.");
        }
    }

    private static void simulateWork() {
        try {
            System.out.println(Thread.currentThread() + " get the lock and do working......");
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
