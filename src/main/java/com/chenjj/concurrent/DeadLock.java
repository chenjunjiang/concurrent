package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/20 0020.
 * 假设一个线程进入对象X的监视器，另一个线程进入对象Y的监视器。如果X中的线程试图调用对象Y的任何同步方法，
 * 那么会如你所期望的那样被阻塞。但是，如果对象Y的线程也试图调用对象A的任何同步方法，那么会永远等待下去。
 * 因为为了进入X，必须释放对Y加的锁，这样第一个线程才能完成。
 */
public class DeadLock implements Runnable{
    DeadLockA deadLockA = new DeadLockA();
    DeadLockB deadLockB = new DeadLockB();

    public DeadLock(){
        Thread.currentThread().setName("Main thread");
        Thread thread = new Thread(this,"RacingThread");
        thread.start();
        deadLockA.foo(deadLockB);
        System.out.println("Back in main thread");
    }

    @Override
    public void run() {
        deadLockB.foo(deadLockA);
        System.out.println("Back in other thread");
    }

    public static void main(String[] args) {
        new DeadLock();
    }
}
