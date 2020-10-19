package com.chenjj.concurrent.atomic;

import sun.misc.Unsafe;
import sun.misc.VM;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;

public class UnsafeTest {
    /**
     * 这样获取unsafe会报错：
     * java.lang.ExceptionInInitializerError
     * 通过getUnsafe()方法的源码，我们可以得知，如果调用该方法的类不是被根类加载器加载的就会抛出异常，
     * 通常情况下开发者所开发的Java类都会被App类加载器进行加载。
     */
    // private static final Unsafe unsafe = Unsafe.getUnsafe();
    public static void main(String[] args) throws Exception {
        // Integer是被根类加载器加载的
        System.out.println(VM.isSystemDomainLoader(Integer.class.getClassLoader()));
        // UnsafeTest是被App ClassLoader加载的
        System.out.println(VM.isSystemDomainLoader(UnsafeTest.class.getClassLoader()));

        /**
         * Unsafe可以绕过构造函数完成对象的创建
         * 我们分别使用new关键字以及反射获得了Example对象的实例，这都会触发无参构造函数的执行，
         * x的值将会被赋予10，因此断言肯定能够顺利通过。
         * 借助于Unsafe的allocateInstance方法获得了Example的实例，该操作并不会导致Example构造函数的执行，
         * 因此x将不会被赋予10。
         */
        Example example1 = new Example();
        assert example1.getX() == 10;
        // 反射方式创建
        Example example2 = Example.class.newInstance();
        assert example2.getX() == 10;
        // 使用Unsafe
        Example example3 = (Example) getUnsafe().allocateInstance(Example.class);
        assert example3.getX() == 0;
        System.out.println("=================================");

        /**
         * 直接修改内存数据
         */
        Guard guard = new Guard();
        assert !guard.canAccess(10);
        assert guard.canAccess(1);
        Unsafe unsafe = getUnsafe();
        Field field = guard.getClass().getDeclaredField("accessNo");
        // 使用unsafe获得field的内存偏移量，然后直接进行内存操作，将accessNo的值修改为20
        unsafe.putInt(guard, unsafe.objectFieldOffset(field), 20);
        assert guard.canAccess(20);

        System.out.println("=================================");

        /**
         * 借助于Unsafe还可以实现对类的加载
         */
        byte[] classContents = getClassContent();
        Class clazz = getUnsafe().defineClass(null, classContents, 0, classContents.length, null, null);
        Object result = clazz.getMethod("getI").invoke(clazz.newInstance(), null);
        assert (Integer) result == 10;
    }

    private static byte[] getClassContent() throws Exception {
        File file = new File("D:\\classloader\\com\\chenjj\\concurrent\\atomic\\UnsafeA.class");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            inputStream.read(content);
            return content;
        }
    }

    /**
     * 通过这种方式来获取Unsafe实例
     *
     * @return
     * @throws Exception
     */
    private static Unsafe getUnsafe() throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    static class Example {
        private int x;

        public Example() {
            this.x = 10;
        }

        public int getX() {
            return x;
        }
    }

    static class Guard {
        private int accessNo = 1;

        public boolean canAccess(int no) {
            return this.accessNo == no;
        }
    }
}
