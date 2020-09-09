package com.chenjj.concurrent.singleton;

/**
 * Double-Check
 * 这种方式看起来既满足了懒加载，又满足了instance的唯一性，但是在多线程环境下还是有可能会出现问题：
 * instance = new Singleton4();
 * 实例化这样一个对象可以分为三个步骤：
 * 1、分配内存空间。
 * 2、初始化对象。
 * 3、将内存空间的地址赋值给对应的引用(此时instance已经不为null了)。
 * 但是由于操作系统可以对指令进行重排序，所以上面的过程也可能会变成如下过程：
 * 1、分配内存空间。
 * 2、将内存空间的地址赋值给对应的引用(此时instance已经不为null了)。
 * 3、初始化对象
 * 如果是这个流程，多线程环境下获得的instance就可能是一个未初始化的对象，在使用这个对象的时候会导致不可预料的结果。
 * 因此，为了防止这个过程的重排序，我们需要将变量设置为volatile类型的变量。
 */
public class Singleton4 {
    // 实例变量
    private byte[] data = new byte[1024];
    // 定义实例，但是不直接初始化
    // private static Singleton4 instance = null;
    private static volatile Singleton4 instance = null;

    private Singleton4() {
    }

    public static synchronized Singleton4 getInstance() {
        // 当instance为null时，才进入同步代码块，不为null直接就返回了，不需要每次进来都加锁，提高了效率
        if (instance == null) {
            // 可能多个线程同时执行到这里，所以必须再次判null
            synchronized (Singleton4.class) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }
}
