package com.chenjj.concurrent.threadSync;

import java.util.List;
import java.util.concurrent.TimeoutException;

public interface Lock {
    /**
     * 永远被阻塞，除非获得了锁，这一点和synchronized非常类似，但是该方法是可中断的，中断时会抛出InterruptedException
     *
     * @throws InterruptedException
     */
    void lock() throws InterruptedException;

    /**
     * 该方法除了可以被中断外，还增加了对应的超时功能
     *
     * @param mills
     * @throws InterruptedException
     * @throws TimeoutException
     */
    void lock(long mills) throws InterruptedException, TimeoutException;

    /**
     * 该方法可以进行锁的释放
     */
    void unlock();

    /**
     * 获取当前被阻塞的线程
     *
     * @return
     */
    List<Thread> getBlockedThreads();
}
