package com.chenjj.concurrent.classloader;

/**
 * 在准备阶段会为静态变量分配内存并设置初始值(是默认值，而不是代码中指定的值)，静态变量被分配到方法区中。
 * 初始化阶段会为静态变量设置所期望的值(代码中指定的值)。
 */
public class Singleton {
    private static Singleton instance = new Singleton();
    private static int x = 0;
    private static int y;

    // 静态语句块只能对后面的静态变量赋值，但是不能对其进行访问
    static {
        // System.out.println(z);
        z = 100;
    }
    private static int z = 10;

    private Singleton() {
        x++;
        y++;
    }

    public static Singleton getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Singleton singleton = Singleton.getInstance();
        System.out.println(singleton.x);
        System.out.println(singleton.y);
        System.out.println(singleton.z);
    }
}
