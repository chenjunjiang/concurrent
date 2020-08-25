package com.chenjj.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * 打断一个线程并不等于该线程的生命周期结束。
 * 从Java的API中可以看到，许多声明抛出InterruptedException的方法（例如Thread.sleep(long millis)方法）这些方法在抛出InterruptedException之前，
 * Java虚拟机会先将该线程的中断标识位清除，然后抛出InterruptedException，此时调用isInterrupted()方法将会返回false。
 */
public class TestInterrupt {
    /*public static void main(String[] args) throws InterruptedException {
        // 会进行休眠
        Thread sleepThread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "SleepThread");
        sleepThread.setDaemon(true);

        // 一直忙碌
        Thread busyThread = new Thread(() -> {
            while (true) {

            }
        }, "BusyThread");
        sleepThread.setDaemon(true);

        sleepThread.start();
        busyThread.start();

        // 休眠5秒，让sleepThread和busyThread充分运行
        TimeUnit.SECONDS.sleep(5);
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());
        // 防止sleepThread和busyThread立刻退出，因为它们被设置为守护线程了
        TimeUnit.SECONDS.sleep(2);
    }*/

    public static void main(String[] args) {
        // 判断当前线程是否被中断
        System.out.println("Main thread is interrupted? " + Thread.interrupted());
        // 中断当前线程
        Thread.currentThread().interrupt();
        // 判断当前线程是否已被中断
        System.out.println("Main thread is interrupted? " + Thread.currentThread().isInterrupted());
        try {
            // 当前线程执行可中断方法，这里会立即中断
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            // 捕获中断信号
            System.out.println("I will be interrupted still.");
        }
    }
}
