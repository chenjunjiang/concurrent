package com.chenjj.concurrent.classloader;

/**
 * 把classpath下的MySample.class移动到D:/classloader下，然后执行，输出结果：
 * MySample is loaded by: My classloader
 * from MySample: class com.chenjj.concurrent.classloader.MyCat
 * MyCat is loaded by: sun.misc.Launcher$AppClassLoader@18b4aac2
 * Exception in thread "main" java.lang.NoClassDefFoundError: com/chenjj/concurrent/classloader/MySample
 * 	at com.chenjj.concurrent.classloader.MyCat.<init>(MyCat.java:6)
 * 	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
 * 	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
 * 	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
 * 	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
 * 	at java.lang.Class.newInstance(Class.java:442)
 * 	at com.chenjj.concurrent.classloader.ClassLoaderAccessTest.main(ClassLoaderAccessTest.java:9)
 * Caused by: java.lang.ClassNotFoundException: com.chenjj.concurrent.classloader.MySample
 * 	at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
 * 	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
 * 	at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:338)
 * 	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
 * 	... 7 more
 */
public class ClassLoaderAccessTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        MyClassLoader myClassLoader = new MyClassLoader();
        Class mySampleClass = myClassLoader.loadClass("com.chenjj.concurrent.classloader.MySample");
        Class myCatClass = myClassLoader.loadClass("com.chenjj.concurrent.classloader.MyCat");
        mySampleClass.newInstance();
        myCatClass.newInstance();
    }
}
