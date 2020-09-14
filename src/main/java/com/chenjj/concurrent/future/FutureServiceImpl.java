package com.chenjj.concurrent.future;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主要作用在于当提交任务时创建一个新的线程来受理该任务，进而达到异步执行的效果
 *
 * @param <IN>
 * @param <OUT>
 */
public class FutureServiceImpl<IN, OUT> implements FutureService<IN, OUT> {
    private final static String FUTURE_THREAD_PREFIX = "FUTURE-";
    private final AtomicInteger nextCounter = new AtomicInteger(0);

    private String getNextName() {
        return FUTURE_THREAD_PREFIX + nextCounter.getAndIncrement();
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        final FutureTask<Void> futureTask = new FutureTask<>();
        new Thread(() -> {
            runnable.run();
            // 任务执行结束后将null作为返回结果传给Future
            futureTask.finish(null);
        }, getNextName()).start();
        return futureTask;
    }

    @Override
    public Future<OUT> submit(Task<IN, OUT> task, IN input) {
        final FutureTask<OUT> futureTask = new FutureTask<>();
        new Thread(() -> {
            OUT result = task.get(input);
            // 任务执行结束后将结果传给Future
            futureTask.finish(result);
        }, getNextName()).start();
        return futureTask;
    }

    @Override
    public Future<OUT> submit(Task<IN, OUT> task, IN input, Callback<OUT> callback) {
        final FutureTask<OUT> futureTask = new FutureTask<>();
        new Thread(() -> {
            OUT result = task.get(input);
            // 任务执行结束后将结果传给Future
            futureTask.finish(result);
            // 执行回调接口
            if (callback != null) {
                callback.call(result);
            }
        }, getNextName()).start();
        return futureTask;
    }
}
