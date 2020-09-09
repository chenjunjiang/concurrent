package com.chenjj.concurrent.singleton;

/**
 * 饿汉式
 * instance作为类变量(静态变量)在类初始化的过程中被收集进<clinit>()方法中，该方法能够百分之百地保证同步，也就是说
 * instance在多线程环境下不会被实例化两次；instance被创建的时候会导致Singleton1的实例变量(data)被初始化。
 */
public final class Singleton1 {
    // 实例变量
    private byte[] data = new byte[1024];
    // 在定义实例对象的时候直接初始化
    private static Singleton1 instance = new Singleton1();

    private Singleton1() {
    }

    public static Singleton1 getInstance() {
        return instance;
    }
}
