package com.chenjj.concurrent.future;

public class FutureTask<T> implements Future<T> {
    // 计算结果
    private T result;
    // 任务是否完成
    private boolean isDone = false;
    // 定义对象锁
    private final Object LOCK = new Object();

    @Override
    public T get() throws InterruptedException {
        synchronized (LOCK) {
            // 当任务还没完成时，调用get方法会被挂起
            while (!isDone) {
                LOCK.wait();
            }
        }
        return result;
    }

    @Override
    public boolean done() {
        return isDone;
    }

    /**
     * 为FutureTask设置计算结果
     *
     * @param result
     */
    protected void finish(T result) {
        synchronized (LOCK) {
            if (isDone) {
                return;
            }
            // 计算完成
            this.result = result;
            this.isDone = true;
            LOCK.notifyAll();
        }
    }
}
