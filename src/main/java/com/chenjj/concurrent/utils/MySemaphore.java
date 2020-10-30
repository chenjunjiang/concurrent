package com.chenjj.concurrent.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * MySemaphore类是扩展自Semaphore的一个子类，该类中有一个重要的队列，该队列为线程安全的队列，
 * 那么，为什么要使用线程安全的队列呢？因为对MySemaphore的操作是由多个线程进行的。
 * 该队列主要用于管理操作Semaphore的线程引用，成功获取到许可证的线程将会被加入该队列之中，
 * 同时只有在该队列中的线程才有资格进行许可证的释放动作。
 * 这样你就不用担心try...finally语句块的使用会引起没有获取到许可证的线程释放许可证的逻辑错误了。
 */
public class MySemaphore extends Semaphore {
    private final ConcurrentLinkedQueue<Thread> queue = new ConcurrentLinkedQueue<>();

    public MySemaphore(int permits) {
        super(permits);
    }

    public MySemaphore(int permits, boolean fair) {
        super(permits, fair);
    }

    @Override
    public void acquire() throws InterruptedException {
        super.acquire();
        // 线程成功获取许可证，将其放入队列
        this.queue.add(Thread.currentThread());
    }

    @Override
    public void acquireUninterruptibly() {
        super.acquireUninterruptibly();
        // 线程成功获取许可证，将其放入队列
        this.queue.add(Thread.currentThread());
    }

    @Override
    public boolean tryAcquire() {
        final boolean acquired = super.tryAcquire();
        if (acquired) {
            // 线程成功获取许可证，将其放入队列
            this.queue.add(Thread.currentThread());
        }
        return acquired;
    }

    @Override
    public void release() {
        final Thread thread = Thread.currentThread();
        // 如果当前队列中不存在该线程，调用release方法无效
        if (!this.queue.contains(thread)) {
            return;
        }
        super.release();
        // 成功释放，将当前线程从队列中删除
        this.queue.remove(thread);
    }

    @Override
    public void acquire(int permits) throws InterruptedException {
        super.acquire(permits);
        // 线程成功获取许可证，将其放入队列
        this.queue.add(Thread.currentThread());
    }

    @Override
    public void acquireUninterruptibly(int permits) {
        super.acquireUninterruptibly(permits);
        // 线程成功获取许可证，将其放入队列
        this.queue.add(Thread.currentThread());
    }

    @Override
    public boolean tryAcquire(int permits) {
        final boolean acquired = super.tryAcquire(permits);
        if (acquired) {
            // 线程成功获取许可证，将其放入队列
            this.queue.add(Thread.currentThread());
        }
        return acquired;
    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        final boolean acquired = super.tryAcquire(timeout, unit);
        if (acquired) {
            // 线程成功获取许可证，将其放入队列
            this.queue.add(Thread.currentThread());
        }
        return acquired;
    }

    @Override
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException {
        final boolean acquired = super.tryAcquire(permits, timeout, unit);
        if (acquired) {
            // 线程成功获取许可证，将其放入队列
            this.queue.add(Thread.currentThread());
        }
        return acquired;
    }

    @Override
    public void release(int permits) {
        final Thread thread = Thread.currentThread();
        // 如果当前队列中不存在该线程，调用release方法无效
        if (!this.queue.contains(thread)) {
            return;
        }
        super.release(permits);
        // 成功释放，将当前线程从队列中删除
        this.queue.remove(thread);
    }
}
