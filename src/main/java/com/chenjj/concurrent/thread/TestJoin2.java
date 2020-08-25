package com.chenjj.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 *如果一个线程已经结束了生命周期，那么调用它的join方法的当前线程不会阻塞。
 */
public class TestJoin2 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "执行完毕");
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.join();
        System.out.println(Thread.currentThread().getName() + "执行完毕");
    }
}
