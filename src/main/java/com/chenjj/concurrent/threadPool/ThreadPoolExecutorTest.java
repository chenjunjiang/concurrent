package com.chenjj.concurrent.threadPool;

import java.util.concurrent.*;

/**
 * 下面的程序在执行以后，JVM进程不会退出，由于我们创建了线程池，因此这就意味着在线程池中有指定数量的活跃线程，
 * JVM进程正常退出最关键的条件之一是在JVM进程中不存在任何运行着的非守护线程。
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        // 提交异步任务，不关注返回值
        executor.execute(() -> System.out.println("execute the runnable task."));
        // 提交异步任务，关注返回值
        Future<String> future = executor.submit(() -> "execute the callable task and this is the result");
        System.out.println(future.get());
    }
}
