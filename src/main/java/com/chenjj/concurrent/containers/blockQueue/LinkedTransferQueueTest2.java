package com.chenjj.concurrent.containers.blockQueue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class LinkedTransferQueueTest2 {
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();
        queue.add("hello");
        queue.offer("world");
        new Thread(() -> {
            // 立即返回false
            assert !queue.tryTransfer("Alex");
            System.out.println("current thread exit.");
        }).start();
        TimeUnit.SECONDS.sleep(2);
        // Alex并未插入至队尾
        assert queue.size() == 2;
    }
}
