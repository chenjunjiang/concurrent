package com.chenjj.concurrent.classloader;

import java.io.IOException;
import java.io.InputStream;

public class ClassLoadPS {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        /*Parent parent = new Parent();
        System.out.println(parent);*/
        // Son son = new Son();
        /*ClassLoader classLoader = ClassLoadPS.class.getClassLoader();
        Class clazz = classLoader.loadClass("com.chenjj.concurrent.classloader.Simple");
        Object object = clazz.newInstance();
        // 强转的时候Parent会被加载
        Parent parent = (Parent) object;*/


        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                System.out.println("---------------------"+name);
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    // 如果当前要加载的class没有在classpath下，就委托给双亲加载
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Class clazz = classLoader.loadClass("com.chenjj.concurrent.classloader.Son");
    }
}
