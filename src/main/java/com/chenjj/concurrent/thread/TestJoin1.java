package com.chenjj.concurrent.thread;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Thread的join方法也是一个可中断的方法，也就是说，如果有其它线程执行了对当前线程的interrupt操作，它也会捕捉到
 * 中断信号，并且擦除线程的interrupt标识。
 * join某个线程A，会使当前线程B进入等待，直到线程A结束生命周期，或者到达给定的时间，那么在此期间线程B是处于
 * WAITING状态的，而不是A线程。
 */
public class TestJoin1 {
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = IntStream.range(1, 3).mapToObj(TestJoin1::create).collect(Collectors.toList());

        threads.forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "#" + i);
            shortSleep();
        }

    }

    private static Thread create(int seq) {
        return new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "#" + i);
                shortSleep();
            }
        });
    }

    private static void shortSleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
