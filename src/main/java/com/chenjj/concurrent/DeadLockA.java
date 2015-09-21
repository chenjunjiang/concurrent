package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/20 0020.
 */
public class DeadLockA {
    public synchronized void foo(DeadLockB deadLockB) {

        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " entered A.foo");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("trying to call B.last()");

        deadLockB.last();
    }

    public synchronized void last(){
        System.out.println("inside A.last");
    }
}
