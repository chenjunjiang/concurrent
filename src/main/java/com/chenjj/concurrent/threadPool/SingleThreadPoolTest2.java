package com.chenjj.concurrent.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这种方式只是让Thread里面的run方法按顺序执行，下边3个线程其实并没有启动。
 * 记住，Thread是实现了Runnable接口的
 */
public class SingleThreadPoolTest2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread());
            System.out.println("T1 normal execute.");
        });

        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread());
            System.out.println("T2 normal execute.");
        });

        Thread thread3 = new Thread(() -> {
            System.out.println(Thread.currentThread());
            System.out.println("T3 normal execute.");
        });

        /*thread1.start();
        thread2.start();
        thread3.start();*/

        executorService.submit(thread1);
        executorService.submit(thread2);
        executorService.submit(thread3);

        executorService.shutdown();
    }
}
