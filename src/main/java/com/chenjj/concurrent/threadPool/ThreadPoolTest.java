package com.chenjj.concurrent.threadPool;

import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        final ThreadPool threadPool = new BasicThreadPool(2, 6, 4, 1000, 1);
        // 定义20个任务并提交给线程池
        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(Thread.currentThread().getName() + " is running and done.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 不断输出线程池的信息
        /*for (; ; ) {
            System.out.println("activeCount:" + threadPool.getActiveCount());
            System.out.println("queueSize:" + threadPool.getQueueSize());
            System.out.println("coreSize:" + threadPool.getCoreSize());
            System.out.println("maxSize:" + threadPool.getMaxSize());
            System.out.println("================================");
            TimeUnit.SECONDS.sleep(1);
        }*/

        TimeUnit.SECONDS.sleep(100);
        threadPool.shutdown();
        // main线程自己join自己，导致一直wait，不退出
        Thread.currentThread().join();
    }
}
