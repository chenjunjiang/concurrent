package com.chenjj.concurrent.threadPool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * 运行下面的程序你会发现，最先完成的任务将会存入阻塞队列之中，因此调用者线程可以立即处理从阻塞队列中得到的
 * 异步任务的运算结果，并进行下一步的操作。
 */
public class CompletionServiceTest {
    public static void main(String[] args) {
        batchTaskDefect();
    }

    private static void batchTaskDefect() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletionService completionService = new ExecutorCompletionService(executorService);
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
        // 提交所有任务
        tasks.forEach(completionService::submit);
        for (int i = 0; i < tasks.size(); i++) {
            try {
                // take方法会阻塞
                System.out.println(completionService.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
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
