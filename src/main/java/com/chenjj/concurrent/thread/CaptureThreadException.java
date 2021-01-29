package com.chenjj.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * 通过源码(逻辑处理都在Thread里面)我们可以知道线程处理unchecked异常的过程如下：
 * 1、当线程在运行过程中出现异常时，JVM会调用Thread的dispatchUncaughtException方法；
 * 2、getUncaughtExceptionHandler方法首先会判断当前线程是否设置了handler，如果有则执行handler的uncaughtException方法，
 * 否则就到当前线程所在的ThreadGroup中获取，ThreadGroup同样也实现了UncaughtExceptionHandler接口；
 * 3、在ThreadGroup中，首先判断：该ThreadGroup如果有父ThreadGroup，则直接调用父ThreadGroup的uncaughtException方法；
 * 如果设置了全局默认的UncaughtExceptionHandler(Thread.setDefaultUncaughtExceptionHandler)，则调用其uncaughtException方法；
 * 若既没有父ThreadGroup，也没有设置全局默认的UncaughtExceptionHandler，则会直接将异常堆栈信息定向到System.err中。
 */
public class CaptureThreadException {
    public static void main(String[] args) {
        // 设置全局UncaughtExceptionHandler，该JVM进程中的所有线程出现unchecked异常后都会回调UncaughtExceptionHandler的uncaughtException方法
        /*Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " occur exception");
            e.printStackTrace();
        });*/
        final Thread thread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 执行......");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 这里出现unchecked异常
            System.out.println(1 / 0);
        }, "Test-Thread");

        // 设置thread的UncaughtExceptionHandler，只对thread有效
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("current thread:" + Thread.currentThread().getName()); // Test-Thread
            System.out.println(t.getName() + " occur exception"); // Test-Thread
        });
        thread.start();

        final Thread thread1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 执行......");
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 这里出现unchecked异常
            System.out.println(1 / 0);
        }, "Test-Thread1");
        thread1.start();
    }
}
