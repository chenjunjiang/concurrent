package com.chenjj.concurrent.threadSync;

import java.util.concurrent.TimeUnit;

/**
 * synchronized同步某个类的不同静态方法，争抢的也是同一个monitor的lock，
 * 与该monitor关联的引用是ClassMonitor.class的实例。
 */
public class ClassMonitor {
    public static synchronized void method1() {
        System.out.println(Thread.currentThread().getName() + " enter to method1");
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void method2() {
        System.out.println(Thread.currentThread().getName() + " enter to method2");
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(ClassMonitor::method1, "T1").start();
        new Thread(ClassMonitor::method2, "T2").start();
    }
}
