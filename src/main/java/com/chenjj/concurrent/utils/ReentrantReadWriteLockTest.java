package com.chenjj.concurrent.utils;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    // 创建读锁
    private final Lock readLock = readWriteLock.readLock();
    // 创建写锁
    private final Lock writeLock = readWriteLock.writeLock();

    private final LinkedList<String> list = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        final ReentrantReadWriteLockTest test = new ReentrantReadWriteLockTest();
        test.list.addLast("hello");
        new Thread(() -> {
            test.get(0);
        }).start();
        TimeUnit.SECONDS.sleep(1);
        test.get(0);
    }

    /**
     * 使用写锁进行数据同步
     *
     * @param element
     */
    public void add(String element) {
        writeLock.lock();
        try {
            TimeUnit.SECONDS.sleep(10);
            list.addLast(element);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 使用写锁进行数据同步
     *
     * @return
     */
    public String take() {
        writeLock.lock();
        try {
            TimeUnit.SECONDS.sleep(10);
            return list.removeFirst();
        } catch (InterruptedException e) {
            return "";
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 使用读锁进行数据同步
     *
     * @param index
     * @return
     */
    public String get(int index) {
        readLock.lock();
        try {
            TimeUnit.SECONDS.sleep(10);
            return list.get(index);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "";
        } finally {
            readLock.unlock();
        }
    }
}
