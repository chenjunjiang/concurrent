package com.chenjj.concurrent.threadPool;

/**
 * 用于线程池内部，它会用到RunnableQueue，然后不断从queue中取出某个Runnable，并执行Runnable的run方法
 */
public class InternalTask implements Runnable {
    private final RunnableQueue runnableQueue;
    private volatile boolean running = true;

    public InternalTask(RunnableQueue runnableQueue) {
        this.runnableQueue = runnableQueue;
    }

    @Override
    public void run() {
        // 如果当前任务为running并且没有被中断，则不断地从RunnableQueue中获取Runnable执行run方法
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Runnable task = runnableQueue.take();
                task.run();
            } catch (Exception e) {
                running = false;
                break;
            }
        }
    }

    /**
     * 停止当前任务，该方法主要会在线程池的方法中使用
     */
    public void stop() {
        running = false;
    }
}
