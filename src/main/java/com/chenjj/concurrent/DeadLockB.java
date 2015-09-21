package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/20 0020.
 */
public class DeadLockB {

    public synchronized void foo(DeadLockA deadLockA) {

        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " entered B.foo");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("trying to call A.last()");

        deadLockA.last();
    }

    public synchronized void last(){
        System.out.println("inside B.last");
    }
}
