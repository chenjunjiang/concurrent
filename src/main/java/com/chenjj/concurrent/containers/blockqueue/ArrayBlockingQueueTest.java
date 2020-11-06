package com.chenjj.concurrent.containers.blockqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ArrayBlockingQueueTest {
    public static void main(String[] args) {
        // 阻塞队列
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        // 定义11个生产数据的线程，向队列的尾部写入数据
        IntStream.rangeClosed(0, 10).boxed().map(i -> new Thread("P-Thread-" + i) {
            @Override
            public void run() {
                while (true) {
                    try {
                        String data = String.valueOf(System.currentTimeMillis());
                        queue.put(data);
                        System.out.println(Thread.currentThread() + " produce data:" + data);
                        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
                    } catch (InterruptedException e) {
                        System.out.println("Received the interrupt SIGNAL.");
                        break;
                    }
                }
            }
        }).forEach(Thread::start);

        // 定义11个消费线程，从队列的头部移除数据
        IntStream.rangeClosed(0, 10).boxed().map(i -> new Thread("C-Thread-" + i) {
            @Override
            public void run() {
                while (true) {
                    try {
                        String data = queue.take();
                        System.out.println(Thread.currentThread() + " consume data:" + data);
                        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
                    } catch (InterruptedException e) {
                        System.out.println("Received the interrupt SIGNAL.");
                        break;
                    }
                }
            }
        }).forEach(Thread::start);
    }
}
