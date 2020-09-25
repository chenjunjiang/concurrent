package com.chenjj.concurrent.reference;

import java.lang.ref.WeakReference;

/**
 * 如果一个对象与GC Roots之间仅存在弱引用，则称这个对象为弱可达对象。
 * Java中可以作为GC Roots的对象:
 * 虚拟机栈（栈帧中的本地变量表）中引用的对象
 * 方法区中类静态属性引用的对象
 * 方法区中常量引用的对象
 * 本地方法栈中JNI（即一般说的native方法）中引用的对象
 *
 * 只要GC就会导致弱引用关联的对象被回收。
 */
public class WeakReferenceTest {
    public static void main(String[] args) {
        Reference reference = new Reference();
        WeakReference<Reference> weakReference = new WeakReference<>(reference);
        // 让Reference对象失去强引用
        reference = null;
        // the reference will be gc.
        System.gc();
        // null
        System.out.println(weakReference.get());
    }
}
