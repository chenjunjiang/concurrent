package com.chenjj.concurrent.threadPool;

/**
 * 用于存放提交到线程池的Runnable，它是一个BlockedQueue，并且有limit的限制。
 */
public interface RunnableQueue {
    // 当前新的任务进来时首先会offer到队列中
    void offer(Runnable runnable);

    // 工作线程通过take方法获取Runnable
    Runnable take() throws InterruptedException;

    // 获取任务队列中的任务数量
    int size();
}
