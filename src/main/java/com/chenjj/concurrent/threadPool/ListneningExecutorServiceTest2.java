package com.chenjj.concurrent.threadPool;

import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ListneningExecutorServiceTest2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
        ListenableFuture<String> listenableFuture = listeningExecutorService.submit(() -> {
            TimeUnit.SECONDS.sleep(10);
            return "I am the result";
        });
        // 注册回调函数
        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String result) {
                System.out.println("The task completed.");
                System.out.println("The task result:" + result);
                listeningExecutorService.shutdown();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, listeningExecutorService);

        System.out.println("主线程不用等待，可以做其它事情......");
    }
}
