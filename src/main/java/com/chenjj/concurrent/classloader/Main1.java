package com.chenjj.concurrent.classloader;

import java.lang.reflect.Method;

public class Main1 {
    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread() + " Main1......");
        // 直接调用Main2的main方法
        Main2.main(args);
        /*Class<?> mainClass = Thread.currentThread().getContextClassLoader().loadClass("com.chenjj.concurrent.classloader.Main2");
        Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
        mainMethod.invoke(null, new Object[]{args});*/
    }
}
