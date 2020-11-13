package com.chenjj.concurrent.threadPool;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ListneningExecutorServiceTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
        ListenableFuture<String> listenableFuture = listeningExecutorService.submit(() -> {
            TimeUnit.SECONDS.sleep(10);
            return "I am the result";
        });
        /*try {
            // 单独调用get会阻塞，直到任务返回结果
            String result = listenableFuture.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        // 注册回调函数
        listenableFuture.addListener(() -> {
            System.out.println("The task completed.");
            try {
                System.out.println("The task result:" + listenableFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("The task failed.");
                e.printStackTrace();
            }
            listeningExecutorService.shutdown();
        }, listeningExecutorService);
    }
}
