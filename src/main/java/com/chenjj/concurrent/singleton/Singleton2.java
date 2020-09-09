package com.chenjj.concurrent.singleton;

/**
 * 懒汉式
 * 在使用实例的时候再去创建，这样就避免了类在初始化时提前创建
 * 在多线程环境下此种方式并不能保证instance的唯一性
 */
public final class Singleton2 {
    // 实例变量
    private byte[] data = new byte[1024];
    // 定义实例，但是不直接初始化
    private static Singleton2 instance = null;

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}
