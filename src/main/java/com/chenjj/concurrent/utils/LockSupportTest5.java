package com.chenjj.concurrent.utils;

import java.util.concurrent.locks.LockSupport;

/**
 * "main" #1 prio=5 os_prio=0 tid=0x0000000002793000 nid=0x209c waiting on condition [0x000000000268f000]
 *    java.lang.Thread.State: WAITING (parking)
 *         at sun.misc.Unsafe.park(Native Method)
 *         - parking to wait for  <0x00000000d6fccde8> (a com.chenjj.concurrent.utils.LockSupportTest5)
 *         at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 *         at com.chenjj.concurrent.utils.LockSupportTest5.testPark(LockSupportTest5.java:18)
 *         at com.chenjj.concurrent.utils.LockSupportTest5.main(LockSupportTest5.java:13)
 */
public class LockSupportTest5 {
    public static void main(String[] args) {
        LockSupportTest5 lockSupportTest5 = new LockSupportTest5();
        System.out.println("begin park!");
        lockSupportTest5.testPark();
        System.out.println("end park!");
    }

    public void testPark() {
        /**
         * Thread类里面有个变量volatile Object parkBlocker，用来存放park方法传递的blocker对象，
         * 也就是把blocker变量存放到了调用park方法的线程的成员变量里面。
         */
        LockSupport.park(this);
    }
}
