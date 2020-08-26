package com.chenjj.concurrent.threadSync;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * 正常运行，锁的效果和synchronized非常类似
 * <p>
 * Thread[Thread-0,5,main] get the lock.
 * Thread[Thread-9,5,main] get the lock.
 * Thread[Thread-1,5,main] get the lock.
 * Thread[Thread-8,5,main] get the lock.
 * Thread[Thread-2,5,main] get the lock.
 * Thread[Thread-7,5,main] get the lock.
 * Thread[Thread-3,5,main] get the lock.
 * Thread[Thread-6,5,main] get the lock.
 * Thread[Thread-4,5,main] get the lock.
 * Thread[Thread-5,5,main] get the lock.
 */
public class BooleanLockTest {
    private final Lock lock = new BooleanLock();

    public void syncMethod() {
        // 加锁
        try {
            lock.lock();
            System.out.println(Thread.currentThread() + " get the lock.");
            int randomInt = ThreadLocalRandom.current().nextInt(10);
            TimeUnit.SECONDS.sleep(randomInt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public void syncMethodTimeout() {
        try {
            lock.lock(1000);
            System.out.println(Thread.currentThread() + " get the lock.");
            int randomInt = ThreadLocalRandom.current().nextInt(10);
            TimeUnit.SECONDS.sleep(randomInt);
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BooleanLockTest test = new BooleanLockTest();
        // 正常测试，启动10个线程
        // IntStream.range(0, 10).mapToObj(i -> new Thread(test::syncMethod)).forEach(Thread::start);

        // 中断被阻塞的线程
        /*new Thread(test::syncMethod, "T1").start();
        TimeUnit.MILLISECONDS.sleep(2);
        Thread t2 = new Thread(test::syncMethod, "T2");
        t2.start();
        TimeUnit.MILLISECONDS.sleep(10);
        t2.interrupt();*/

        // 测试阻塞线程超时
        new Thread(test::syncMethodTimeout, "T1").start();
        TimeUnit.MILLISECONDS.sleep(2);
        Thread t2 = new Thread(test::syncMethodTimeout, "T2");
        t2.start();
        TimeUnit.MILLISECONDS.sleep(10);
    }
}
