package com.chenjj.concurrent.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * synchronized关键字存在缺陷，当某个线程在争抢对象监视器（object monitor）的时候将会进入阻塞状态，
 * 并且是无法被中断的，也就是说synchronized关键字并未提供一种获取monitor锁失败的通知机制，
 * 执行线程只能等待其他线程释放该monitor的锁进而得到一次机会，
 * 我们将借助于AtomicBoolean实现一个可立即返回并且退出阻塞的显式锁(这相当于是个乐观锁)。
 */
public class TryLock {
    private final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private final ThreadLocal<Boolean> threadLocal = ThreadLocal.withInitial(() -> false);

    /**
     * 加锁，未获得锁的线程不会被阻塞
     *
     * @return
     */
    public boolean tryLock() {
        boolean result = atomicBoolean.compareAndSet(false, true);
        if (result) {
            // 成功获得锁
            threadLocal.set(true);
        }
        return result;
    }

    /**
     * 释放锁
     *
     * @return
     */
    public boolean release() {
        // 如果当前线程已经成功获得了锁
        if (threadLocal.get()) {
            threadLocal.set(false);
            return atomicBoolean.compareAndSet(true, false);
        } else {
            return false;
        }
    }
}
