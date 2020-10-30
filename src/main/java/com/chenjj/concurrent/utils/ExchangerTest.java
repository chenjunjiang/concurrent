package com.chenjj.concurrent.utils;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 下面的代码中定义了两个线程T1和T2，分别调用Exchanger的exchange方法将各自的数据传递给对方，
 * 在这里需要注意的是，每个线程在构造数据时的开销是不一样的，因此调用方法exchange的时机并不是同一时刻，
 * 当T1线程在执行exchange方法的时候，如果T2方法没有执行exchange方法，
 * 那么T1线程会进入阻塞状态等待T2线程执行exchange方法，只有当两个线程都执行了exchange方法之后，
 * 它们才会退出阻塞。exchange方法是一个阻塞方法，只有成对的线程执行了exchange调用之后才会退出阻塞。
 */
public class ExchangerTest {
    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> {
            System.out.println(Thread.currentThread() + " start.");
            // 随机休眠
            randomSleep();
            try {
                // 调用exchange方法，将对应的数据传递给T2线程，同时从T2线程获取交换的数据
                String data = exchanger.exchange("I am from T1");
                System.out.println(Thread.currentThread() + " received:" + data);
                // 可以多次调用exchange进行数据交换
                data = exchanger.exchange("I am from T1 2");
                System.out.println(Thread.currentThread() + " 2 received:" + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " end.");
        }, "T1").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread() + " start.");
            // 随机休眠
            randomSleep();
            try {
                // 调用exchange方法，将对应的数据传递给T1线程，同时从T1线程获取交换的数据
                String data = exchanger.exchange("I am from T2");
                System.out.println(Thread.currentThread() + " received:" + data);
                data = exchanger.exchange("I am from T2 2");
                System.out.println(Thread.currentThread() + " 2 received:" + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " end.");
        }, "T2").start();
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
