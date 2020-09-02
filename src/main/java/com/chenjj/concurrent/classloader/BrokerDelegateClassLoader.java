package com.chenjj.concurrent.classloader;

/**
 * 通过重写loadClass方法来破坏双亲委托机制
 */
public class BrokerDelegateClassLoader extends MyClassLoader {
    public BrokerDelegateClassLoader() {
    }

    public BrokerDelegateClassLoader(String classDir) {
        super(classDir);
    }

    public BrokerDelegateClassLoader(String classDir, ClassLoader parent) {
        super(classDir, parent);
    }

    /*@Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 到已加载类的缓存中查看该类是否已经被加载
            Class<?> clazz = findLoadedClass(name);
            if (clazz == null) {
                if (name.startsWith("java.") || name.startsWith("javax.")) {
                    try {
                        clazz = getSystemClassLoader().loadClass(name);
                    } catch (Exception e) {
                        // ignore
                    }
                } else {// 如果要加载类的全限定名不是以java或javax开头，则尝试用自定义的类加载器加载
                    try {
                        clazz = this.findClass(name);
                    } catch (ClassNotFoundException e) {
                        // ignore
                    }
                    // 若自定义类加载器仍旧没有完成对类的加载，则委托其父类加载器或系统类加载器进行加载
                    if (clazz == null) {
                        if (getParent() != null) {
                            clazz = getParent().loadClass(name);
                        } else {
                            clazz = getSystemClassLoader().loadClass(name);
                        }
                    }
                }
            }
            if (clazz == null) {
                throw new ClassNotFoundException("The class " + name + " not found.");
            }
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
    }*/

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 到已加载类的缓存中查看该类是否已经被加载
            Class<?> clazz = findLoadedClass(name);
            if (clazz == null) {
                try {
                    // 自己加载
                    clazz = this.findClass(name);
                } catch (ClassNotFoundException e) {
                    // ignore
                }
                // 若自定义类加载器仍旧没有完成对类的加载，则委托其父类加载器或系统类加载器进行加载
                if (clazz == null) {
                    if (getParent() != null) {
                        clazz = getParent().loadClass(name);
                    } else {
                        clazz = getSystemClassLoader().loadClass(name);
                    }
                }
            }
            if (clazz == null) {
                throw new ClassNotFoundException("The class " + name + " not found.");
            }
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
    }
}
