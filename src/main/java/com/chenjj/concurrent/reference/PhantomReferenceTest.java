package com.chenjj.concurrent.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;

/**
 * 如果一个对象与GC Roots之间仅存在虚引用，则称这个对象为虚可达（phantom reachable）对象。
 * 虚引用有以下特点：
 * 1、PhantomReference必须和ReferenceQueue配合使用
 * 2、PhantomReference的get方法返回值始终是null
 * 3、当垃圾回收器决定回收PhantomReference关联的对象的时候，会将PhantomReference插入关联的ReferenceQueue中
 * 4、使用PhantomReference进行清理动作要比Object的finalize方法更加灵活
 *
 * phantomReference在放入队列之前会先调用Reference的tryHandlePending方法，该方法里面会进行入队操作
 */
public class PhantomReferenceTest {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<Reference> queue = new ReferenceQueue<>();
        Reference reference = new Reference();
        /**
         * 这里创建出PhantomReference的实例之后，必须进行引用，并且还要保证phantomReference在reference被垃圾回收的时候还在，
         * 否则phantomReference不会放入到queue中
         */
        PhantomReference phantomReference = new PhantomReference<>(reference, queue);
        // System.out.println(phantomReference);
        // 始终返回null
        // System.out.println(phantomReference.get());
        reference = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        /**
         * 这里要特别注意：如果在Reference中重写了finalize方法，那么GC后phantomReference不会被放到queue中，
         * 只有在没重写finalize方法的情况下，GC后phantomReference才会被放到queue中
         */
        java.lang.ref.Reference<? extends Reference> gcdRef = queue.remove();
        System.out.println(gcdRef);
    }
}
