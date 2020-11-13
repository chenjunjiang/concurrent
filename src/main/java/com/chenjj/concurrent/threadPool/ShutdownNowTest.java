package com.chenjj.concurrent.threadPool;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 工作线程会被尝试中断，很明显，我们的任务中存在可被中断的方法调用，因此任务会被成功中断。
 */
public class ShutdownNowTest {
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
        // 返回未被执行的任务
        List<Runnable> remainingRunnable = executor.shutdownNow();
        System.out.println(remainingRunnable.size());
    }
}
