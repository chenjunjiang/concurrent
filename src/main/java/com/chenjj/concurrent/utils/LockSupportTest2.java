package com.chenjj.concurrent.utils;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest2 {
    public static void main(String[] args) {
        System.out.println("begin park!");
        // 使当前线程获取到许可证，调用多次也只能获取一个许可证
        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());
        // 调用park方法，立即返回
        LockSupport.park();
        System.out.println("end park!");
        // 再次调用，又会挂起
        LockSupport.park();
    }
}
