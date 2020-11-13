package com.chenjj.concurrent.threadPool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTest {
    public static void main(String[] args) {
        CompletableFuture<Double> completableFuture = new CompletableFuture<>();
        // 提交异步任务
        Executors.newCachedThreadPool().submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("Task completed.");
                completableFuture.complete(123.45D);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 非阻塞获取异步任务计算结果，很明显由于异步任务还没执行完，结果值是默认的
        assert completableFuture.getNow(0.0D) == 0.0D;
        try {
            assert completableFuture.get() == 123.45D;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
