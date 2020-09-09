package com.chenjj.concurrent.singleton;

/**
 * Holder方式
 * 借助类加载的特点
 * 这种方式在Singleton5初始化的时候并不会创建instance，只有当Holder被主动引用的时候才会被创建；
 * instance的创建过程是在Java程序编译时期被收集到了<clinit>()方法中，该方法又是同步方法，同步方法可以保证内存
 * 的可见性、JVM指令的顺序性和原子性。
 */
public final class Singleton5 {
    // 实例变量
    private byte[] data = new byte[1024];

    private Singleton5() {
    }

    // 在静态内部类中持有Singleton5的实例，并且可被直接初始化
    private static class Holder {
        private static Singleton5 instance = new Singleton5();
    }

    public static Singleton5 getInstance() {
        return Holder.instance;
    }
}
