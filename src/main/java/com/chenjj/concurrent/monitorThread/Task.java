package com.chenjj.concurrent.monitorThread;

/**
 * 该接口与Runnable的作用类似，真正的任务逻辑在call方法里面执行
 *
 * @param <T>
 */
@FunctionalInterface
public interface Task<T> {
    // 任务执行接口，允许有返回值
    T call();
}
