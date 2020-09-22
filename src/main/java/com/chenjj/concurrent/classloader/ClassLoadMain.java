package com.chenjj.concurrent.classloader;

import com.chenjj.concurrent.threadSync.Account;

public class ClassLoadMain {

    public static void main(String[] args) {
        System.out.println("ClassLoadMain......");
        /*Simple[] simples = new Simple[10];
        System.out.println(simples.length);*/
        //System.out.println(GlobalConstants.MAX);
        //System.out.println(GlobalConstants.RANDOM);
        Class clazz = Account.class.getClass();
    }
}
