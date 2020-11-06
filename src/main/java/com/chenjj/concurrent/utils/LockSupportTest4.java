package com.chenjj.concurrent.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * park方法有以下三个原因会返回：
 * Some other thread invokes unpark with the current thread as the target; or
 * Some other thread interrupts the current thread; or
 * The call spuriously (that is, for no reason) returns.(假唤醒)
 * 由于park方法返回时我们不知道是哪种原因返回，所以调用时需要根据之前调用park方法的原因，
 * 再次检查条件是否满足，如果不满足则还需要再次调用park方法。
 */
public class LockSupportTest4 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park!");
            // 调用park挂起，只有被中断后才会退出循环
            while (!Thread.currentThread().isInterrupted()) {
                // 被中断后park直接返回，不会抛出异常
                LockSupport.park();
                // 即使下边的main线程执行了LockSupport.unpark(thread); 由于不满足while条件，所以会继续调用park挂起
                System.out.println("child thread unPark1!");
            }
            System.out.println("child thread unPark2!");
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("main thread begin unPark!");
        LockSupport.unpark(thread);
        // 中断子线程
        thread.interrupt();
    }
}
