package com.chenjj.concurrent.future;

public interface FutureService<IN, OUT> {
    // 提交不需要返回值的任务，Future.get方法返回值将会是null
    Future<?> submit(Runnable runnable);

    // 提交需要返回值的任务，其中Task接口代替了Runnable接口
    Future<OUT> submit(Task<IN, OUT> task, IN input);

    // 增加回调接口，任务执行完成后callback会执行
    Future<OUT> submit(Task<IN, OUT> task, IN input, Callback<OUT> callback);

    // 使用静态方法创建一个FutureService的实例
    static <T, R> FutureService<T, R> newService() {
        return new FutureServiceImpl<>();
    }
}
