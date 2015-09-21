package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/19 0019.
 */
public class Queue {

    private int number;

    private boolean flag = false;

    public synchronized void put(int number) {
        /**
         * 尽管在正常情况下，wait方法会等待直到调用notify或notifyAll方法，
         * 但是还有一种几率很小却可能会发生的情况，等待线程由于假唤醒(spurious wakeup)
         * 而被唤醒。对于这种情况，等待线程也会被换唤醒，然后却没有调用notify或notifyAll方法
         * (本质上，线程在没有什么明显理由的情况下就被恢复了)。因为存在这种极小的可能，Oracle推荐应当在
         * 一个检查线程等待条件的循环中调用wait方法。
         */
        while (flag) {
            try {
                // wait方法通知调用线程放弃监视器并进入休眠，直到其他一些线程进入同一个监视器并调用notify方法
                // 或notifyAll方法
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("put：" + number);
        this.number = number;
        flag = true;
        // notify方法唤醒调用相同对象的wait方法的线程
        notify();
    }

    public synchronized int get() {
        while (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("get：" + this.number);
        flag = false;
        notify();
        return this.number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
