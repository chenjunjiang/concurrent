package com.chenjj.concurrent.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ExecuteVSSubmitTest {
    public static void main(String[] args) {
        // ExecutorService executorService = Executors.newFixedThreadPool(1);
        /**
         * Submits a Runnable task for execution and returns a Future representing that task.
         * The Future's get method will return null upon successful completion.
         * 执行之后发现没有任何异常信息输出，直接被吞掉了。
         * 如果需要输出异常信息需要使用future.get()
         */
        /*Future future = executorService.submit(() -> {
            System.out.println("执行任务......");
            System.out.println(1 / 0);
        });
        try {
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /**
         * 执行之后发现异常信息能够正常输出
         * 但它也仅仅是输出而已，我们无法使用logback之类的日志框架对其进行记录，
         * 因为它这个打印动作我们是不可控的。
         * 如果想要执行这个任务的线程能捕获这个异常，可以使用UncaughtExceptionHandler。
         * 当然，我觉得更好的方式是我们自己在任务里面处理这些异常，以为我们更清楚
         * 每个不同任务的异常处理方式。
         */
       /* executorService.execute(() -> {
            System.out.println("执行任务......");
            System.out.println(1 / 0);
        });
        executorService.shutdown();*/

        ThreadFactory factory = r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setUncaughtExceptionHandler((t, e) -> {
                System.out.println(t + "" + e);
                e.printStackTrace();//example
            });
            return thread;
        };
        ExecutorService executorService = Executors.newFixedThreadPool(1, factory);
        executorService.execute(() -> {
            System.out.println("执行任务......");
            System.out.println(1 / 0);
        });
        executorService.shutdown();
    }
}
