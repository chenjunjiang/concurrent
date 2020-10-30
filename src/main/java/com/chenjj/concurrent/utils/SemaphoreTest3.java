package com.chenjj.concurrent.utils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * release在设计并未强制要求执行release操作的线程必须是执行了acquire的线程才可以，
 * 而是需要开发人员自身具有相应的编程约束来确保Semaphore的正确使用。
 */
public class SemaphoreTest3 {
    public static void main(String[] args) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(1, true);
        Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire();
                System.out.println("The thread t1 acquired permit from semaphore.");
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("The thread t1 interrupted");
            } finally {
                semaphore.release();
            }
        });
        t1.start();

        TimeUnit.SECONDS.sleep(1);

        Thread t2 = new Thread(() -> {
            try {
                semaphore.acquire();
                System.out.println("The thread t2 acquired permit from semaphore.");
            } catch (InterruptedException e) {
                System.out.println("The thread t2 interrupted.");
                // 出现异常后不再往下执行，否则就会执行下面的semaphore.release();导致许可证的错误释放
                return;
            } finally {
                semaphore.release();
            }
        });
        t2.start();

        TimeUnit.SECONDS.sleep(2);
        t2.interrupt();

        semaphore.acquire();
        System.out.println("The main thread acquired permit.");
    }
}
