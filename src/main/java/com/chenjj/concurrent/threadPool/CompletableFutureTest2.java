package com.chenjj.concurrent.threadPool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CompletableFutureTest2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 异步执行Supplier类型的任务
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 353);
        // 允许指定ExecutorService
        // CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()-> 353, Executors.newCachedThreadPool());
        assert future.get() == 353;

        // 异步执行Runnable类型的任务
        CompletableFuture.runAsync(() -> System.out.println("async task."));
        // 允许指定ExecutorService
        CompletableFuture.runAsync(() -> System.out.println("async task."), Executors.newCachedThreadPool());
    }
}
