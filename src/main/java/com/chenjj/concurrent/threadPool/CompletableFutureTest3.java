package com.chenjj.concurrent.threadPool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // thenApply会以同步的方式继续处理上一个异步任务的结果
        /*CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            // Thread[pool-1-thread-1,5,main]
            System.out.println("supplyAsync:" + Thread.currentThread());
            return "Java";
        }, executorService).thenApply(e -> {
            // thenApply:Thread[main,5,main]
            System.out.println("thenApply:" + Thread.currentThread());
            return e.length();
        });*/

        // thenApplyAsync：以异步的方式继续处理上一个异步任务的结果
        /*CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            // supplyAsync:Thread[pool-1-thread-1,5,main]
            System.out.println("supplyAsync:" + Thread.currentThread());
            return "Java";
        }, executorService).thenApplyAsync(e -> {
            // thenApplyAsync:Thread[pool-1-thread-2,5,main]
            System.out.println("thenApplyAsync:" + Thread.currentThread());
            return e.length();
        }, executorService);

        assert future.get() == 4;*/

        //  thenAccept：以同步的方式消费上一个异步任务的结果，不再返回结果
        /*CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            // supplyAsync:Thread[pool-1-thread-1,5,main]
            System.out.println("supplyAsync:" + Thread.currentThread());
            return "Java";
        }, executorService).thenAccept(e -> {
            // thenAccept:Thread[main,5,main]
            System.out.println("thenAccept:" + Thread.currentThread());
            System.out.println(e);
        });*/

        // thenAcceptAsync：以异步的方式消费上一个异步任务的结果，不再返回结果
        /*CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            // supplyAsync:Thread[pool-1-thread-1,5,main]
            System.out.println("supplyAsync:" + Thread.currentThread());
            return "Java";
        }, executorService).thenAcceptAsync(e -> {
            // thenAcceptAsync:Thread[pool-1-thread-2,5,main]
            System.out.println("thenAcceptAsync:" + Thread.currentThread());
            System.out.println(e);
        }, executorService);*/

        // thenRun：以同步的方式执行Runnable任务
        /*CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            // supplyAsync:Thread[pool-1-thread-1,5,main]
            System.out.println("supplyAsync:" + Thread.currentThread());
            return "Java";
        }, executorService).thenAccept(e -> {
            // thenAccept:Thread[main,5,main]
            System.out.println("thenAccept:" + Thread.currentThread());
            System.out.println(e);
        }).thenRun(() -> System.out.println("All of task completed." + Thread.currentThread()));*/
        // All of task completed.Thread[main,5,main]

        // thenRunAsync：以异步的方式执行Runnable任务
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            // supplyAsync:Thread[pool-1-thread-1,5,main]
            System.out.println("supplyAsync:" + Thread.currentThread());
            return "Java";
        }, executorService).thenAcceptAsync(e -> {
            // thenAcceptAsync:Thread[pool-1-thread-2,5,main]
            System.out.println("thenAcceptAsync:" + Thread.currentThread());
            System.out.println(e);
        }, executorService).thenRunAsync(() -> System.out.println("All of task completed." + Thread.currentThread())
                , executorService);
        // All of task completed.Thread[pool-1-thread-3,5,main]

        executorService.shutdown();
    }
}
