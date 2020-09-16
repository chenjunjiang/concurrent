package com.chenjj.concurrent.threadContext;

import java.lang.ref.WeakReference;

public class WeakReferenceTest {
    public static void main(String[] args) {
        // user变量是强引用
        User user = new User();
        user.setAge(20);
        user.setName("chenjunjiang");
        WeakReference<User> weakReference = new WeakReference<>(user);
        // 让前面的User对象失去强引用
        user = null;
        System.out.println(weakReference.get()); // User{age=20, name='chenjunjiang'}
        // gc的时候如果一个对象只是被弱引用，那么该对象会被回收；如果对象有被强引用的情况，那么gc是不会回收这个对象的
        System.gc();
        System.out.println(weakReference.get()); // null
    }
}
