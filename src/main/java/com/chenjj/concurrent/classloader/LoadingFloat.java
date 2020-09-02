package com.chenjj.concurrent.classloader;

/**
 * 我们使用自定义类加载器加载自己定义的java.lang.Float类，虽然能够找到这个类，但是JVM是不允许这样做的，会报：
 * Exception in thread "main" java.lang.SecurityException: Prohibited package name: java.lang，
 * 打开ClassLoader的preDefineClass方法就会发现，JVM做了安全性校验：
 * if ((name != null) && name.startsWith("java.")) {
 * throw new SecurityException
 * ("Prohibited package name: " +
 * name.substring(0, name.lastIndexOf('.')));
 * }
 */
public class LoadingFloat {
    public static void main(String[] args) throws ClassNotFoundException {
        BrokerDelegateClassLoader classLoader = new BrokerDelegateClassLoader("D:\\classloader");
        classLoader.loadClass("java.lang.Float");
    }
}
