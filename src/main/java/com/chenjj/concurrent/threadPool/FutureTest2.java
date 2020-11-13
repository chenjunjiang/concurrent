package com.chenjj.concurrent.threadPool;

import java.util.concurrent.*;

public class FutureTest2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Double> future = executorService.submit(() -> {
            throw new RuntimeException();
        });
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // 这里的异常就是上边异步线程执行任务时抛出的RuntimeException
            e.printStackTrace();
        }
    }
}
