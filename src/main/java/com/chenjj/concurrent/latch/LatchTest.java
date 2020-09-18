package com.chenjj.concurrent.latch;

import java.util.concurrent.TimeUnit;

public class LatchTest {
    public static void main(String[] args) throws InterruptedException {
        Latch latch = new CountDownLatch(4);
        new ProgrammerTravel(latch, "Alex", "Bus").start();
        new ProgrammerTravel(latch, "Gavin", "Walking").start();
        new ProgrammerTravel(latch, "Jack", "Subway").start();
        new ProgrammerTravel(latch, "Dillon", "Bicycle").start();
        // 当前线程(main)等待，直到4个ProgrammerTravel线程全部执行完成
        // latch.await();
        // System.out.println("=== all of programmer arrived ===");
        try {
            // 5秒后只要还有一个程序员没到达目的地，就抛出超时异常
            latch.await(TimeUnit.SECONDS, 5);
            System.out.println("=== all of programmer arrived ===");
        } catch (WaitTimeOutException e) {
            e.printStackTrace();
        }

    }
}
