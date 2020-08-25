package com.chenjj.concurrent.threadSync;

import java.util.concurrent.TimeUnit;

/**
 *synchronized关键字修饰同一个实例对象的两个不同方法，它们争抢的是同一个monitor的lock，与之关联的引用则是
 * ThisMonitor的实例引用。
 */
public class ThisMonitor {
    public synchronized void method1() {
        System.out.println(Thread.currentThread().getName() + " enter to method1");
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void method2() {
        System.out.println(Thread.currentThread().getName() + " enter to method2");
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ThisMonitor thisMonitor = new ThisMonitor();
        new Thread(thisMonitor::method1, "T1").start();
        new Thread(thisMonitor::method2, "T2").start();
    }
}
