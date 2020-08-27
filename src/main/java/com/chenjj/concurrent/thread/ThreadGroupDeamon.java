package com.chenjj.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * 原理可以看README.md
 */
public class ThreadGroupDeamon {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup group1 = new ThreadGroup("Group1");
        new Thread(group1, () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "group1-thread1").start();

        ThreadGroup group2 = new ThreadGroup("Group2");
        new Thread(group2, () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "group2-thread1").start();

        group2.setDaemon(true);
        System.out.println(group2.isDestroyed()); // false
        TimeUnit.SECONDS.sleep(3);
        System.out.println(group1.isDestroyed()); // false
        System.out.println(group2.isDestroyed()); // true
    }
}
