package com.chenjj.concurrent.monitorThread;

/**
 * 该接口与Runnable的作用类似
 * @param <T>
 */
@FunctionalInterface
public interface Task<T> {
    // 任务执行接口，允许有返回值
    T call();
}
