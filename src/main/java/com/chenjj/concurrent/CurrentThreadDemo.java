package com.chenjj.concurrent;

public class CurrentThreadDemo {

    public static void main(String[] args) {
        Thread currentThread = Thread.currentThread();
        System.out.println("Current thread：" + currentThread);
        currentThread.setName("My Thread");
        System.out.println("After name change：" + currentThread);

        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
    }
}
