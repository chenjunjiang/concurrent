package com.chenjj.concurrent.threadPool;

import java.util.concurrent.*;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ShutdownAndAwaitTerminationTest {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                SecurityManager s = System.getSecurityManager();
                ThreadGroup group = (s != null) ? s.getThreadGroup() :
                        Thread.currentThread().getThreadGroup();
                String namePrefix = "pool-" +
                        poolNumber.getAndIncrement() +
                        "-thread-";
                Thread t = new Thread(group, r,
                        namePrefix + threadNumber.getAndIncrement(),
                        0);
                t.setDaemon(true);
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        }, new ThreadPoolExecutor.DiscardPolicy());

        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                executor.execute(() -> {
                    System.out.println(Thread.currentThread() + " is running.");
                    // 故意模拟耗时且不可中断操作
                    while (true) {

                    }
                });
            } else {
                executor.execute(() -> {
                    System.out.println(Thread.currentThread() + " is running.");
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        shutdownAndAwaitTermination(executor, 1, TimeUnit.MINUTES);
    }

    static void shutdownAndAwaitTermination(ExecutorService executor, long timeout, TimeUnit unit) {
        executor.shutdown();
        try {
            // 如果在指定时间内线程池仍旧未关闭
            if (!executor.awaitTermination(timeout, unit)) {
                executor.shutdownNow();
                /**
                 * 如果线程池中的工作线程正在执行一个非常耗时且不可中断的方法，则中断失败
                 * 为了避免这种情况的发送，我们线程池中的线程可以使用守护线程，这样就不会导致程序整个程序不能退出了
                 */
                if (!executor.awaitTermination(timeout, unit)) {
                    System.out.println("有非常耗时且不可中断的方法在执行, 请检查");
                }
            }
        } catch (InterruptedException e) {
            // 如果当前线程被中断，立即执行shutdownNow
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
