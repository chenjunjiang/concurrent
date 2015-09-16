package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/17 0017.
 */
public class ThreadDemo {
    public static void main(String args[]) {
        new NewThread();
        for (int i = 5; i > 0; i--) {
            System.out.println("Main thread：" + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted");
            }
        }

        System.out.println("Main thread exiting.");
    }

}
