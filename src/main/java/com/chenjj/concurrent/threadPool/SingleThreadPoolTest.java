package com.chenjj.concurrent.threadPool;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 当SingleThreadPool引用可被垃圾回收器回收时，线程池的shutdown方法会被执行
 */
public class SingleThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        singleThreadPool();
        printThreadStack();
        System.out.println("=====================");
        // 显示调用GC
        System.gc();
        TimeUnit.MINUTES.sleep(1);
        // 再次打印线程栈的时候，已经找不到线程池中的线程了，说明已经调用了线程池的shutdown方法
        printThreadStack();
    }

    private static void singleThreadPool() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> System.out.println("normal task."));
    }

    private static void printThreadStack() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] ids = threadMXBean.getAllThreadIds();
        for (long id : ids) {
            System.out.println(threadMXBean.getThreadInfo(id));
        }
    }
}
