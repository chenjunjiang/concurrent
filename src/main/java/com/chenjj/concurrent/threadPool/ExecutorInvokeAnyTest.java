package com.chenjj.concurrent.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorInvokeAnyTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 定义一批任务
        List<Callable<Integer>> callables = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            callables.add(() -> {
                int random = ThreadLocalRandom.current().nextInt(30);
                // 模拟不同接口调用花费不同的时间
                TimeUnit.SECONDS.sleep(random);
                System.out.println("Task:" + random + " completed in Thread " + Thread.currentThread());
                return random;
            });
        }

        // 批量提交任务，但是只关心第一个完成任务返回的结果
        int result = executorService.invokeAny(callables);
        System.out.println("Result:" + result);
    }
}
