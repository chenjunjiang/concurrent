package com.chenjj.concurrent.utils;

import java.util.concurrent.TimeUnit;

public class SemaphoreTest4 {
    public static void main(String[] args) throws InterruptedException {
        final MySemaphore semaphore = new MySemaphore(1, true);
        Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire();
                System.out.println("The thread t1 acquired permit from semaphore.");
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("The thread t1 interrupted.");
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
            } finally {
                /**
                 * 由于使用的是自定义的MySemaphore，所以即使t2被打断后继续执行release，也不会释放许可证，因为
                 * t2没有获取到许可证，它不能释放，直接就返回了。
                 */
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
