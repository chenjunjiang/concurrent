package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/17 0017.
 */
public class NewThread implements Runnable{

    Thread thread;

    NewThread(){
        thread = new Thread(this,"Demo Thread");
        System.out.println("Child Thread："+thread);
        thread.start();
    }

    @Override
    public void run() {

        for (int i = 5; i>0 ; i--) {
            System.out.println("Child Thread："+i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Child interrupted");
            }
        }

    }
}
