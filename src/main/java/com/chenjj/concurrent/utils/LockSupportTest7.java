package com.chenjj.concurrent.utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class LockSupportTest7 {
    public static void main(String[] args) {
        final FIFOMutex fifoMutex = new FIFOMutex();
        IntStream.range(0, 10).forEach(i -> {
            new Thread(() -> {
                // 获取锁
                fifoMutex.lock();
                System.out.println(Thread.currentThread().getName() + " get the lock!");
                try {
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " release the lock!");
                fifoMutex.unlock();
            }, "Thread-" + i).start();
        });
    }
}
