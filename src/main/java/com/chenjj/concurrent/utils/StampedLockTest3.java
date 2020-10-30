package com.chenjj.concurrent.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 乐观读模式
 * StampedLock还提供了一个模式，即乐观读模式，使用tryOptimisticRead()方法获取一个非排他锁并且不会进入
 * 阻塞状态，与此同时该模式依然会返回一个long型的数据戳用于接下来的验证（该验证主要用来判断共享资源是否有
 * 写操作发生）
 */
public class StampedLockTest3 {
    private static int shareData = 0;
    private static final StampedLock lock = new StampedLock();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            inc();
        }, "write").start();
        TimeUnit.SECONDS.sleep(1);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                get();
            }, "read-" + i).start();
            TimeUnit.SECONDS.sleep(2);
        }
    }

    public static void inc() {
        long stamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread() + " acquired the lock.");
            // 修改共享数据
            shareData++;
            System.out.println(Thread.currentThread() + " change data: " + shareData);
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlockWrite(stamp);
        }
    }

    /**
     * get方法首先进行了一次乐观读锁的获取并且立即返回一个数据戳（stamp），
     * 但是仅就这样的操作是不足以立即将共享数据返回的，这会导致数据出现不一致的情况，具体说明如下：
     * · 假设调用乐观读返回的数据戳（stamp）为零，则代表其他线程正在对共享资源进行写操作，
     * 也就是说其他线程获取了对该共享资源的写权限。
     * · 假设调用乐观读返回的数据戳（stamp）为非零，紧接着又有其他线程立即获取了对共享资源的写操作。
     * 基于以上两点，我们还需要对数据戳（stamp）进行校验之后才能决定对共享资源进行阻塞式的读还是将其立即返回，
     * 使用StampedLock的validate方法可以判断上述两种情况是否发生。
     * ▪ 如果上述两种情况已经发生，则进行读锁的获取操作，此时若有其他线程对共享线程进行写操作，
     * 则当前线程会进入阻塞等待直到获取到读锁。
     * ▪ 如果获取的stamp通过验证，则直接返回共享数据，不进行任何同步操作，这样的话就可以对共享数据进行无锁（Lock-Free）读操作了，
     * 即提高了共享资源并发读取的能力。
     *
     * @return
     */
    public static int get() {
        // 这里会立即返回，不会导致当前线程阻塞
        long stamp = lock.tryOptimisticRead();
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                System.out.println(Thread.currentThread() + " acquired the lock.");
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread() + " get data: " + shareData);
                return shareData;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        System.out.println(Thread.currentThread() + " do not need to get lock.");
        return shareData;
    }
}
