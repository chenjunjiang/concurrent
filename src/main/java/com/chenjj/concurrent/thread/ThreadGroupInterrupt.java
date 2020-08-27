package com.chenjj.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * 原理可以看README.md
 */
public class ThreadGroupInterrupt {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup group = new ThreadGroup("TestGroup");
        new Thread(group, () -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println("t1 will exit.");
        }, "t1").start();

        new Thread(group, () -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println("t2 will exit.");
        }, "t2").start();

        TimeUnit.MILLISECONDS.sleep(2);

        group.interrupt();
    }
}
