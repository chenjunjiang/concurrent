package com.chenjj.concurrent.singleton;

/**
 * 枚举方式
 * 枚举类型不允许被继承，同样是线程安全的且只能被实例化一次，但是不能够实现懒加载
 */
public enum Singleton6 {
    INSTANCE;
    // 实例变量
    private byte[] data = new byte[1024];

    // 枚举的构造方法默认就是private
    Singleton6() {
        System.out.println("INSTANCE will be initialized immediately");
    }

    public static void method() {
        // 调用该方法会主动使用Singleton6，INSTANCE将会被实例化
    }

    public static Singleton6 getInstance() {
        return INSTANCE;
    }
}
