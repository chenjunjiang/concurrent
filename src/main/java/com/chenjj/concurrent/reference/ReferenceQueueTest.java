package com.chenjj.concurrent.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * 无论是SoftReference还是WeakReference引用，当它们关联的对象被垃圾回收器回收后，
 * SoftReference和WeakReference的实例都会被存放到与之关联的ReferenceQueue中
 */
public class ReferenceQueueTest {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<Reference> queue = new ReferenceQueue<>();
        Reference reference = new Reference();
        // 这里创建出WeakReference的实例之后，必须进行引用，否则后面GC后不会往queue中放入WeakReference实例
        WeakReference<Reference> weakReference = new WeakReference<>(reference, queue);
        // 失去强引用
        reference = null;
        System.out.println(weakReference.get());
        System.gc();
        // 保证GC线程能执行
        TimeUnit.SECONDS.sleep(1);
        // remove会阻塞直到队列有数据，如果把前面的reference = null;注释了，那么对象由于被强引用，gc后也不会被回收，那么
        // weakReference实例就不会被放到queue中，queue没有数据，这里就会一直阻塞
        java.lang.ref.Reference<? extends Reference> gcdRef = queue.remove();
        System.out.println(gcdRef);
        // null，与弱引用关联的对象已经被回收了
        System.out.println(gcdRef.get());
    }
}
