package com.chenjj.concurrent.threadPool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * 我们定义了三个批量任务，很明显，耗时10秒的任务将会第一个被执行完成，但是很遗憾，我们无法立即使用该异步任务运算
 * 所得的结果。原因是在批量任务中存在一个拖后腿的（30秒才能运行结束），因此想要在接下来的程序运行中使用上述批量
 * 任务的结果至少还要等待30秒的时间，这对于耗时较快的任务来说是一种非常不必要的等待。
 */
public class ExecutorServiceInvokeAllTest {
    public static void main(String[] args) {
        batchTaskDefect();
    }

    private static void batchTaskDefect() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final List<Callable<Integer>> tasks = Arrays.asList(() -> {
                    sleep(30);
                    System.out.println("Task 30 completed.");
                    return 30;
                },
                () -> {
                    sleep(10);
                    System.out.println("Task 10 completed.");
                    return 10;
                }, () -> {
                    sleep(20);
                    System.out.println("Task 20 completed.");
                    return 20;
                });

        try {
            // 阻塞等待所有Future返回
            List<Future<Integer>> futures = executorService.invokeAll(tasks);
            futures.forEach(future -> {
                try {
                    System.out.println(future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
