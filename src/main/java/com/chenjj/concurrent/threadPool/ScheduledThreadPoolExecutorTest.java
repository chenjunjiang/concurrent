package com.chenjj.concurrent.threadPool;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 核心线程数为2，其它参数默认
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        // 延迟10s执行任务
        ScheduledFuture<String> future = executor.schedule(() -> {
            System.out.println("I am running.");
            return "hello";
        }, 10, TimeUnit.SECONDS);
        System.out.println("result:" + future.get());

        // 任务延迟10s后以每隔60s的速率不断执行
        ScheduledFuture<?> future1 = executor.scheduleAtFixedRate(() -> {
            System.out.println(new Date());
        }, 10, 60, TimeUnit.SECONDS);
        /**
         * 不要执行future1.get()方法，否则当前线程会一直阻塞，因为任务会一直运行，可以使用future1取消任务的执行
         */
    }
}
