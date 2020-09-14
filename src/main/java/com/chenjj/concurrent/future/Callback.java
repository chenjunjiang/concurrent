package com.chenjj.concurrent.future;

public interface Callback<T> {
    // 任务完成后调用该方法，并传入执行结果
    void call(T result);
}
