package com.chenjj.concurrent.classloader;

public class LoadingSimpleClass {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        BrokerDelegateClassLoader brokerDelegateClassLoader = new BrokerDelegateClassLoader("D:\\classloader");
        Class<?> clazz = brokerDelegateClassLoader.loadClass("com.chenjj.concurrent.classloader.SimpleClass");
        System.out.println(brokerDelegateClassLoader.getParent());
        clazz.newInstance();
    }
}
