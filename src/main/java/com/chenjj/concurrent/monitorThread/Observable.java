package com.chenjj.concurrent.monitorThread;

/**
 * 该接口主要是暴露给调用者使用的
 */
public interface Observable {
    // 任务的生命周期状态
    enum Cycle{
        STARTED, RUNNING, DONE, ERROR
    }

    // 获取当前任务的状态
    Cycle getCycle();
    // 定义线程的启动方法，主要作用是屏蔽Thread的其它方法
    void start();
    // 定义线程的打断方法，主要作用是屏蔽Thread的其它方法
    void interrupt();
}
