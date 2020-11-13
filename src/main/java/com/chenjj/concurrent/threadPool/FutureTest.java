package com.chenjj.concurrent.threadPool;

import java.util.concurrent.*;

public class FutureTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Double> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task completed.");
            return 53.3d;
        });
        TimeUnit.SECONDS.sleep(10);
        // 取消正在执行的异步任务(参数为false，任务虽然被取消但是不会将其中断)
        System.out.println("cancel success?" + future.cancel(false));
        // true
        System.out.println("future is canceled?" + future.isCancelled());
        // 对一个已经取消的任务执行get方法会抛出异常:java.util.concurrent.CancellationException
        System.out.println("Task result:" + future.get());
    }
}
