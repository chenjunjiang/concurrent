package com.chenjj.concurrent.threadPool;

import java.util.concurrent.*;

public class CompletableFutureTest7 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "test";
        }, executorService);

        // 如果直接调用了complete方法，那就意味着future.get()不会再阻塞，返回值就是这里指定的，
        // 不会再等异步任务执行完了。
        // future.complete("xxx");
        String result = future.get();
        System.out.println("get()方法会阻塞，直到上面提交的异步任务执行结束......" + result);
    }
}
