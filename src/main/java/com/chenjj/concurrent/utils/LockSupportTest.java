package com.chenjj.concurrent.utils;

import java.util.concurrent.locks.LockSupport;

/**
 * 调用park方法，最终只会输出begin park!，然后当前线程被挂起，
 * 这是因为在默认情况下调用线程是不持有许可证的。
 */
public class LockSupportTest {
    public static void main(String[] args) {
        System.out.println("begin park!");
        LockSupport.park();
        System.out.println("end park!");
    }
}
