package com.chenjj.concurrent.utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 使用LockSupport实现一个先进先出的锁，也就是只有队列的首元素可以获取锁
 */
public class FIFOMutex {
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    public void lock() {
        boolean isInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);
        // 如果当前线程不是队首或者当前锁已经被其他线程获取，则调用park方法挂起自己
        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
            // 被中断后park直接返回，不会抛出异常
            LockSupport.park(this);
            // 判断是否中断，然后清除中断标志
            if (Thread.interrupted()) {
                // 做个标记
                isInterrupted = true;
            }
        }
        // 删除队列头部元素
        waiters.remove();
        /**
         * 如果标记为true则中断该线程，这个怎么理解呢？其实就是其他线程中断了该线程，虽然我对中断信号不感兴趣，
         * 忽略它，但是不代表其他线程对该标志不感兴趣，所以要恢复下。
         */
        if (isInterrupted) {
            current.interrupt();
        }
    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}
