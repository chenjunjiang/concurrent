package com.chenjj.concurrent.thread;

import java.util.concurrent.TimeUnit;

public class TestJoin {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        // 线程启动之后再join才有效
        thread.join();
        System.out.println("main is done.");
    }
}
