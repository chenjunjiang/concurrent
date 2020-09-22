package com.chenjj.concurrent.classloader;

import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/**
 * Reflection.getCallerClass()此方法的调用者必须有权限，需要什么样的权限呢？
 * 由bootstrap class loader加载的类可以调用
 * 由extension class loader加载的类可以调用
 * 所以下边的test()执行后会报错
 * Class.forName里面会调用Reflection.getCallerClass()来获取Class对象：
 * Class<?> caller = Reflection.getCallerClass();
 * return forName0(className, true, ClassLoader.getClassLoader(caller), caller);
 * 这里获取到的caller关联的类就是GetCallerClassTest，
 * ClassLoader.getClassLoader(caller)得到的类加载器就是AppClassLoader，由于initialize参数为true，
 * 所以通过Class.forName("xxx")这种方式加载类会导致类的初始化。
 * 当然如果使用Class.forName("xxx",false, classloader)的话是不会导致被加载类的初始化的。
 */
public class GetCallerClassTest {
    static {
        System.out.println("GetCallerClassTest is initialized......");
    }

    public static void main(String[] args) throws ClassNotFoundException {
        // Class.forName("com.chenjj.concurrent.classloader.HelloWorld");
        // Class.forName("com.chenjj.concurrent.classloader.HelloWorld", false, GetCallerClassTest.class.getClassLoader());
        // test();
        ClassLoader classLoader = GetCallerClassTest.class.getClassLoader();
        classLoader.loadClass("com.chenjj.concurrent.classloader.HelloWorld");
    }

    public static void test() {
        System.out.println(Reflection.getCallerClass());
    }
}
