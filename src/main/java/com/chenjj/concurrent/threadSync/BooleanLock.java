package com.chenjj.concurrent.threadSync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 具备synchronized关键字所有的功能的同时又具备可中断和lock超时的功能
 */
public class BooleanLock implements Lock {
    // 当前拥有锁的线程
    private Thread currentThread;
    // false代表该锁没有被任何线程获得或已被释放，true代表该锁已经被某个线程(就是currentThread)获得
    private boolean locked = false;
    // 当前被阻塞的线程
    private final List<Thread> blockedList = new ArrayList<>();

    @Override
    public void lock() throws InterruptedException {
        synchronized (this) {
            while (locked) {
                final Thread tempThread = Thread.currentThread();
                if (!blockedList.contains(tempThread)) {
                    blockedList.add(tempThread);
                }
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    // 如果当前线程在wait时被中断，则从blockedList中将其删除，避免内存泄露
                    blockedList.remove(tempThread);
                    // 继续抛出中断异常
                    throw e;
                }
            }
            // 获得锁了之后从blockedList中删除自己，如果没有自己，执行删除方法没有任何影响
            blockedList.remove(Thread.currentThread());
            this.locked = true;
            this.currentThread = Thread.currentThread();
        }
    }

    @Override
    public void lock(long mills) throws InterruptedException, TimeoutException {
        synchronized (this) {
            if (mills < 0) {
                this.lock();
            } else {
                long remainingMills = mills;
                long endMills = System.currentTimeMillis() + remainingMills;
                while (locked) {
                    // 超时之后还没有获取到锁
                    if (remainingMills <= 0) {
                        throw new TimeoutException("can not get the lock during " + mills);
                    }
                    final Thread tempThread = Thread.currentThread();
                    if (!blockedList.contains(tempThread)) {
                        blockedList.add(tempThread);
                    }
                    try {
                        // 有可能还没到超时时间的时候已经被唤醒
                        this.wait(remainingMills);
                        remainingMills = endMills - System.currentTimeMillis();
                    } catch (InterruptedException e) {
                        // 如果当前线程在wait时被中断，则从blockedList中将其删除，避免内存泄露
                        blockedList.remove(tempThread);
                        // 继续抛出中断异常
                        throw e;
                    }
                }
                blockedList.remove(Thread.currentThread());
                this.locked = true;
                this.currentThread = Thread.currentThread();
            }
        }
    }

    @Override
    public void unlock() {
        synchronized (this) {
            // 只有获取到锁的线程才有资格解锁
            if (currentThread == Thread.currentThread()) {
                this.locked = false;
                this.notifyAll();
            }
        }
    }

    @Override
    public List<Thread> getBlockedThreads() {
        return Collections.unmodifiableList(blockedList);
    }
}
