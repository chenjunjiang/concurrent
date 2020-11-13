package com.chenjj.concurrent.threadPool;

import java.util.concurrent.*;

public class CompletableFutureTest5 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // f1,f2,f3如果不指定线程池的话默认使用的是通过ForkJoinPool.commonPool()创建出来的，这个线程池中的线程是守护线程，这点要特别注意。
        //ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("f1:" + Thread.currentThread());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Java";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("f2:" + Thread.currentThread());
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Parallel";
        });
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("f3:" + Thread.currentThread());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Future";
        });
        /**
         * allOf并行执行任务，会立即返回CompletableFuture，不管是thenRun还是thenRunAsync都会在所有的任务执行完成后再执行，
         * 执行thenRun方法的线程是最后执行完的这个任务所在的线程(这里就是f2所在的线程)
         * thenRunAsync方法执行时不指定线程池的话，默认使用ForkJoinPool.commonPool。
         *anyOf和allOf的区别是：只要有一个任务完成了，就会执行thenRun方法，那么此时
         * 执行thenRun方法的线程是最先执行完的这个任务所在的线程(这里就是f1所在的线程)
         */
        CompletableFuture<Void> completableFuture = CompletableFuture.anyOf(f1, f2, f3).thenRun(() -> {
            try {
                System.out.println(Thread.currentThread());
                System.out.println(f1.isDone() + " and result:" + f1.get());
                System.out.println(f2.isDone() + " and result:" + f2.get());
                System.out.println(f3.isDone() + " and result:" + f3.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        System.out.println("主线程在这里不会被阻塞......");
        //TimeUnit.SECONDS.sleep(40);
        // 阻塞等待运行结果，结果为null，这里的目的是为了让主线程不退出
        System.out.println(completableFuture.get());

        //executorService.shutdown();
    }
}
