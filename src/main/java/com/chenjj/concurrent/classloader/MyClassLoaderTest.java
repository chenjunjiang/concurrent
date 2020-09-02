package com.chenjj.concurrent.classloader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类加载器loadClass并不会导致类的初始化，它只是执行了加载过程中的加载阶段而已。
 * loadClass的逻辑如下：
 * 1、从当前类加载器的已加载类缓存中根据类的全路径名查询是否存在该类，如果存在直接返回。
 * 2、如果当前类存在父类加载器，则调用父类加载器的loadClass方法对其进行加载。
 * 3、如果当前类的所有父加载器都没有成功加载class，则尝试使用当前类加载器的findClass方法对其进行加载，
 * 该方法就是我们自定义类加载器需要重写的方法。
 * 4、最后如果类被成功加载，则做一些性能数据的统计。
 * 5、由于loadClass方法参数指定了resolve为false，所以不会进行连接阶段的继续执行，这也就解释了为什么通过类加载器
 * 加载类并不会导致类的初始化。
 * 根据以上分析，我们下边的自定义类加载器在没有删除HelloWorld.java的情况下，加载HelloWorld用到的是AppClassLoader，
 * 它是MyClassLoader的父加载器，它会加载idea编译后的classpath里面的HelloWorld.class，如果要让MyClassLoader
 * 加载HelloWorld.class，可以有以下方式：
 * 1、删除HelloWorld.java或重命名。
 * 2、绕过AppClassLoader，直接将扩展类加载器(ExtClassLoader)作为MyClassLoader的父加载器，因为ExtClassLoader
 * 和根类加载器的加载路径下都没有HelloWorld.class， 只能交给MyClassLoader来加载。
 * 3、在构造MyClassLoader的时候指定其父类加载器为null。
 */
public class MyClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // MyClassLoader myClassLoader = new MyClassLoader();
       /* ClassLoader extClassLoader = MyClassLoader.class.getClassLoader().getParent();
        MyClassLoader myClassLoader = new MyClassLoader("D:\\classloader", extClassLoader);*/
        // MyClassLoader myClassLoader = new MyClassLoader("D:\\classloader", null);
        // 加载HelloWorld
        BrokerDelegateClassLoader myClassLoader = new BrokerDelegateClassLoader();
        Class<?> clazz = myClassLoader.loadClass("com.chenjj.concurrent.classloader.HelloWorld");
        System.out.println(clazz.getClassLoader());
        System.out.println(clazz.getClassLoader().getParent());
        Object helloWorld = clazz.newInstance();
        System.out.println(helloWorld);
        Method method = clazz.getMethod("welcome");
        String result = (String) method.invoke(helloWorld);
        System.out.println("Result:" + result);
    }
}
