package com.chenjj.concurrent.containers.blockQueue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class LinkedTransferQueueTest3 {
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();
        // 启动三个线程消费queue中的元素(从头部开始)
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " consume data over.");
            }).start();
        }
        // 休眠1s，确保3个线程均已启动并阻塞
        TimeUnit.SECONDS.sleep(1);
        assert queue.hasWaitingConsumer();
        assert queue.getWaitingConsumerCount() == 3;
        queue.offer("test");
        assert queue.hasWaitingConsumer();
        assert queue.getWaitingConsumerCount() == 2;
    }
}
