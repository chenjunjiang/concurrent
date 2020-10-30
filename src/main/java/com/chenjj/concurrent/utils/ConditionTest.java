package com.chenjj.concurrent.utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
    // 共享数据
    private static int shareData = 0;
    // 当前共享数据是否已经被使用
    private static boolean dataUsed = false;
    private static final Lock lock = new ReentrantLock();
    // 创建与lock关联的condition
    private static final Condition condition = lock.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            for (; ; ) {
                change();
            }
        }, "Producer").start();

        new Thread(() -> {
            for (; ; ) {
                use();
            }
        }, "Consumer").start();
    }

    /**
     * 读数据进行写操作
     */
    private static void change() {
        lock.lock();
        try {
            // 如果当前数据未被使用，则当前线程进入wait队列，并且释放lock
            while (!dataUsed) {
                condition.await();
            }
            // 修改数据
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
            shareData++;
            dataUsed = false;
            System.out.println("produce the new value:" + shareData);
            // 唤醒wait队列中的线程-数据使用线程
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 使用数据
     */
    private static void use() {
        lock.lock();
        try {
            // 如果当前数据已经被使用，则当前线程进入wait队列，并且释放lock
            while (dataUsed) {
                condition.await();
            }
            // 使用数据
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
            System.out.println("the shared data changed:" + shareData);
            dataUsed = true;
            // 唤醒wait队列中的线程-数据修改线程
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
