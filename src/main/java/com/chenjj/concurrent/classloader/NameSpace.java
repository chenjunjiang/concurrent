package com.chenjj.concurrent.classloader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 使用同一个类加载器实例，不管load多少次TestSimple，生成的class对象都是同一份。
 * 但是，使用不同的类加载器或者同一个类加载器的不同实例，去加载同一个class，则会在堆内存和方法区中产生多个class对象。
 */
public class NameSpace {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 获取系统类加载器
        ClassLoader classLoader = NameSpace.class.getClassLoader();
        ClassLoader classLoader5 = NameSpace.class.getClassLoader();
        System.out.println(classLoader == classLoader5); // true
        Class<?> aClass = classLoader.loadClass("com.chenjj.concurrent.classloader.TestSimple");
        Class<?> bClass = classLoader.loadClass("com.chenjj.concurrent.classloader.TestSimple");
        System.out.println(aClass.hashCode());
        System.out.println(bClass.hashCode());
        System.out.println(aClass == bClass);// true

        System.out.println("==============================");

        MyClassLoader classLoader1 = new MyClassLoader("D:\\classloader", null);
        BrokerDelegateClassLoader classLoader2 = new BrokerDelegateClassLoader("D:\\classloader", null);
        Class<?> aClass1 = classLoader1.loadClass("com.chenjj.concurrent.classloader.TestSimple");
        Class<?> bClass1 = classLoader2.loadClass("com.chenjj.concurrent.classloader.TestSimple");
        System.out.println(aClass1.getClassLoader());
        System.out.println(bClass1.getClassLoader());
        System.out.println(aClass1.hashCode());
        System.out.println(bClass1.hashCode());
        System.out.println(aClass1 == bClass1);// false

        System.out.println("==============================");
        MyClassLoader classLoader3 = new MyClassLoader("D:\\classloader", null);
        MyClassLoader classLoader4 = new MyClassLoader("D:\\classloader", null);
        Class<?> aClass2 = classLoader3.loadClass("com.chenjj.concurrent.classloader.TestSimple");
        Class<?> bClass2 = classLoader4.loadClass("com.chenjj.concurrent.classloader.TestSimple");
        System.out.println(aClass2.getClassLoader());
        System.out.println(bClass2.getClassLoader());
        System.out.println(aClass2.hashCode());
        System.out.println(bClass2.hashCode());
        System.out.println(aClass2 == bClass2);// false

        System.out.println("==============================");
        Class<?> clazz1 = classLoader3.loadClass("com.chenjj.concurrent.classloader.Student");
        Class<?> clazz2 = classLoader4.loadClass("com.chenjj.concurrent.classloader.Student");
        System.out.println(clazz1 == clazz2);// false
        Object obj1 = clazz1.newInstance();
        Object obj2 = clazz2.newInstance();
        Method method = clazz1.getMethod("setStudent", Object.class);
        /**
         * 执行的时候会抛出：
         * Caused by: java.lang.ClassCastException: com.chenjj.concurrent.classloader.Student cannot be cast to com.chenjj.concurrent.classloader.Student
         * 明明是同一类，而java却告诉我不能转换？这是为什么呢？
         * 因为同一个类被不同的类加载器实例加载了，导致Student有两个不同的Class对象存在于内存中，在不同的命名空间中，
         * 所有由这两个Class对象创建出的两个Student对象相互之间是不可见的，自然不能进行转换。
         */
        method.invoke(obj1, obj2);
    }
}
