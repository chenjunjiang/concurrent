package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/17 0017.
 */
public class Synch {
    public static void main(String args[]) {

        Callme callme = new Callme();
        Caller caller1 = new Caller(callme, "Hello", "Hello");
        Caller caller2 = new Caller(callme, "synchronized", "synchronized");
        Caller caller3 = new Caller(callme, "World", "World");

        try {
            caller1.getThread().join();
            caller2.getThread().join();
            caller3.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
