package com.chenjj.concurrent.latch;

import java.util.concurrent.TimeUnit;

public class CountDownLatch extends Latch {
    private Runnable runnable;

    public CountDownLatch(int limit) {
        super(limit);
    }

    /**
     * 增加回调接口Runnable，所有子任务完成后再执行它
     *
     * @param limit
     * @param runnable
     */
    public CountDownLatch(int limit, Runnable runnable) {
        super(limit);
        this.runnable = runnable;
    }

    /**
     * 该方法会使得当前线程一直等待，直到所有线程都完成工作，被阻塞线程是允许被中断的
     *
     * @throws InterruptedException
     */
    @Override
    public void await() throws InterruptedException {
        synchronized (this) {
            while (limit > 0) {
                this.wait();
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override
    public void await(TimeUnit unit, long time) throws InterruptedException, WaitTimeOutException {
        if (time <= 0) {
            throw new IllegalArgumentException("The time is invalid.");
        }
        long remainingNanos = unit.toNanos(time);
        final long endNanos = System.nanoTime() + remainingNanos;
        synchronized (this) {
            while (limit > 0) {
                // 如果超时则抛出WaitTimeOutException
                if (TimeUnit.NANOSECONDS.toMillis(remainingNanos) <= 0) {
                    throw new WaitTimeOutException("The wait time over specify time.");
                }
                // wait超时方法的参数是毫秒，所以需要转换
                this.wait(TimeUnit.NANOSECONDS.toMillis(remainingNanos));
                remainingNanos = endNanos - System.nanoTime();
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /**
     * 当任务线程完成工作之后调用该方法使得计数器减一
     */
    @Override
    public void countDown() {
        synchronized (this) {
            if (limit < 0) {
                throw new IllegalStateException("all of task already arrived");
            }
            limit--;
            this.notifyAll();
        }
    }

    /**
     * 获取当前还有多少个线程没有完成任务
     *
     * @return
     */
    @Override
    public int getUnArrived() {
        return limit;
    }
}
