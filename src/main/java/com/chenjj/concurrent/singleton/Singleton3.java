package com.chenjj.concurrent.singleton;

/**
 * 懒汉式+同步方法
 * 通过下面的synchronized，在多线程环境下能百分之百保证instance的唯一性，但是每次调用getInstance都会加锁，
 * 在高并发时，性能低下
 */
public class Singleton3 {
    // 实例变量
    private byte[] data = new byte[1024];
    // 定义实例，但是不直接初始化
    private static Singleton3 instance = null;

    private Singleton3() {
    }

    public static synchronized Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}
