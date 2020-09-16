package com.chenjj.concurrent.threadContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ThreadLocalTest {
    public static void main(String[] args) throws InterruptedException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        /*ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        IntStream.range(0, 10).forEach(i -> {
            new Thread(() -> {
                try {
                    // 每个线程都会使用到threadLocal，但是彼此之间的数据是独立的
                    threadLocal.set(i);
                    System.out.println(Thread.currentThread() + " set i " + threadLocal.get());
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread() + " set i " + threadLocal.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
        // 为ThreadLocal初始化一个值，其实就是重写了ThreadLocal的initialValue()方法
        ThreadLocal threadLocal1 = ThreadLocal.withInitial(Object::new);
        System.out.println(threadLocal1.get());*/

        /**
         * 当Thread和ThreadLocal发生绑定之后，对象的引用关系图如"ThreadLocal内存泄露-引用关系图.png"所示，
         * 将threadLocal显示指定为null并执行GC后，由于Entry中的key对ThreadLocal是弱引用，所以此时的threadLocal
         * 会被回收，Entry中的key也变为null，关系图就变为了"ThreadLocal内存泄露-GC后的引用关系图.png"，但是Entry
         * 的value可能不会被回收，因为只要当前线程生命周期不结束，图中最下面的一条引用关系就还存在，这样就出现了内存泄露。
         * 所以用ThreadLocal完成业务逻辑后记得调用threadLocal.remove()，及时释放资源。
         */
        ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();
        TimeUnit.SECONDS.sleep(10);
        // 虽然最终存储的数据以最后一次set为准，但是已经申请了300MB的内存，前200MB内存已经没有引用了，可以被回收，执行GC后就会被回收
        threadLocal.set(new byte[1024 * 1024 * 100]);// 100MB
        threadLocal.set(new byte[1024 * 1024 * 100]);
        threadLocal.set(new byte[1024 * 1024 * 100]);
        threadLocal = null;
        TimeUnit.SECONDS.sleep(10);
        Thread t = Thread.currentThread();
        Class<? extends Thread> clazz = t.getClass();
        Field field = clazz.getDeclaredField("threadLocals");
        field.setAccessible(true);
        Object threadLocalMap = field.get(t);
        Class<?> tlmClass = threadLocalMap.getClass();
        /**
         * Method method=clazz.getDeclaredMethod(name);//可以调用类中的所有方法（不包括父类中继承的方法）
         * Method method=clazz.getMethod(name);//可以调用类中有访问权限的方法（包括父类中继承的方法）
         */
        /*Method method = tlmClass.getDeclaredMethod("getEntry", ThreadLocal.class);
        System.out.println(method);
        method.setAccessible(true);
        method.invoke(threadLocalMap, threadLocal);*/
        Thread.currentThread().join();
    }
}
