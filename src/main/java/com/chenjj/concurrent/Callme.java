package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/17 0017.
 */
public class Callme {
    public synchronized void call(String msg) {
        System.out.print("[" + msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("]");
    }

    public void callNoSynch(Thread thread){
        System.out.println(thread.getName()+"调用了非同步方法");
    }

}
