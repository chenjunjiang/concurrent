package com.chenjj.concurrent.classloader;

public class BootStrapClassLoader {
    public static void main(String[] args) {
        // 由于String.class的类加载器是根加载器，根加载器是获取不到引用的，因此输出null
        System.out.println("BootStrap:" + String.class.getClassLoader());
        // 输出 根加载器的加载路径
        System.out.println(System.getProperty("sun.boot.class.path"));
    }
}
