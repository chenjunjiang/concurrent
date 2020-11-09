package com.chenjj.concurrent.containers.blockQueue;

import java.util.concurrent.SynchronousQueue;
import java.util.stream.IntStream;

public class SynchronousQueueTest {
    public static void main(String[] args) {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        // 启动两个线程，向queue中写入数据
        IntStream.rangeClosed(0, 1).forEach(i -> {
            new Thread(() -> {
                try {
                    // 若没有对应的线程消费数据，则put方法将会导致当前线程阻塞
                    String data = Thread.currentThread().getName();
                    queue.put(data);
                    System.out.println(Thread.currentThread() + " put element " + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        // 启动两个线程，从queue中获取数据
        IntStream.rangeClosed(0, 1).forEach(i -> {
            new Thread(() -> {
                try {
                    // 若没有对应的线程生产数据，则take方法将会导致当前线程阻塞
                    String data = queue.take();
                    System.out.println(Thread.currentThread() + "take " + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
