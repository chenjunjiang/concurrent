package com.chenjj.concurrent.future;

/**
 * Task接口主要是提供给调用者实现计算逻辑用的，可以接收一个参数并且返回最终的计算结果
 */
public interface Task<IN, OUT> {
    OUT get(IN input);
}
