package com.chenjj.concurrent.threadSync;

import java.util.concurrent.TimeUnit;

public class SynchronizedDefect {
    public synchronized void syncMethod() {
        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDefect defect = new SynchronizedDefect();
        Thread t1 = new Thread(defect::syncMethod, "T1");
        t1.start();
        TimeUnit.MILLISECONDS.sleep(2);
        Thread t2 = new Thread(defect::syncMethod, "T2");
        t2.start();
        // 保证线程t2运行，它会因为synchronized而阻塞在syncMethod方法
        TimeUnit.MILLISECONDS.sleep(2);
        // 打断t2，虽然interrupt标识为true， 但是t2还是被阻塞，说明被synchronized同步的线程不可被中断
        t2.interrupt();
        System.out.println(t2.isInterrupted()); // true
        System.out.println(t2.getState()); // BLOCKED
    }
}
