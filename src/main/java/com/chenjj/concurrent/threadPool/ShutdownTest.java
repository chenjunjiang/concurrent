package com.chenjj.concurrent.threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ShutdownTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread() + " is running.");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        System.out.println("shutdown后会立即返回");
        // 线程池被shutdown
        assert executor.isShutdown();
        // 线程池正在结束中
        assert executor.isTerminating();
        // 线程池还未完全结束，因为任务队列中还有任务
        assert !executor.isTerminated();
        // 新提交的任务不被接收
        executor.execute(() -> System.out.println("new task submit after shutdown"));
        // 等待线程池结束，最多10分钟
        executor.awaitTermination(10, TimeUnit.MINUTES);
        assert executor.isShutdown();
        assert !executor.isTerminating();
        assert executor.isTerminated();
    }
}
