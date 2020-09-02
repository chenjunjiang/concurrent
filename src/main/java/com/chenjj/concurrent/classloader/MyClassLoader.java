package com.chenjj.concurrent.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 关于类的全路径格式，一般情况下我们的类都是类似于java.lang.String这样的格式，但是有时不排除内部类，匿名内部类等；
 * 全路径格式有如下几种情况：
 * 1、java.lang.String：包名.类名
 * 2、javax.swing.JSpinner$DefaultEditor：包名.类名$内部类
 * 3、java.security.KeyStore$Builder$FileBuilder$1：包名.类名$内部类$内部类$匿名内部类
 * 4、java.net.URLClassLoader$3$1：包名.类名$匿名内部类$匿名内部类
 */
public class MyClassLoader extends ClassLoader {
    // 默认的class存放路径
    private final static Path DEFAULT_CLASS_DIR = Paths.get("D:", "classloader");
    private final Path classDir;

    public MyClassLoader() {
        super();
        this.classDir = DEFAULT_CLASS_DIR;
    }

    public MyClassLoader(String classDir) {
        super();
        this.classDir = Paths.get(classDir);
    }

    public MyClassLoader(String classDir, ClassLoader parent) {
        super(parent);
        this.classDir = Paths.get(classDir);
    }

    /**
     * 必须重写这个方法
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 读取class的二进制文件
        byte[] classBytes = this.readClassBytes(name);
        if (classBytes == null || classBytes.length == 0) {
            throw new ClassNotFoundException("Can not load the class " + name);
        }
        // 调用父类的defineClass方法定义class
        return this.defineClass(name, classBytes, 0, classBytes.length);
    }

    private byte[] readClassBytes(String name) throws ClassNotFoundException {
        // 将包名分隔符转换成文件路径分隔符
        String classPath = name.replace(".", "/");
        Path classFullPath = classDir.resolve(Paths.get(classPath + ".class"));
        if (!classFullPath.toFile().exists()) {
            throw new ClassNotFoundException("The class " + name + " not found.");
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Files.copy(classFullPath, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ClassNotFoundException("Load the class " + name + " occur error.", e);
        }
    }

    @Override
    public String toString() {
        return "My classloader";
    }
}
