package com.chenjj.concurrent.thread;

import java.util.concurrent.TimeUnit;

public class ThreadDeamon {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("执行任务......");
            }
        });
        thread.setDaemon(true);
        thread.start();

        TimeUnit.SECONDS.sleep(3);
        System.out.println("主线程执行完毕......");
    }
}
