package com.chenjj.concurrent.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class LockSupportTest3 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            System.out.println("child thread begin park1!");
            // 挂起自己
            LockSupport.park();
            System.out.println("child thread unPark1!");
            System.out.println("child thread begin park2!");
            LockSupport.park();
            System.out.println("child thread unPark2!");
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("main thread begin unPark");
        // 调用unpark方法让thread线程持有许可证，park()立即返回
        LockSupport.unpark(thread);
        LockSupport.unpark(thread);
    }
}
