package com.chenjj.concurrent.containers.blockQueue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class LinkedTransferQueueTest {
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();
        // 在队列尾部插入数据
        queue.add("hello");
        queue.offer("world");
        queue.put("Java");
        // 此时该队列的数据元素为(队尾)Java->world->hello
        new Thread(() -> {
            try {
                queue.transfer("Alex");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("current thread exit.");
        }).start();
        TimeUnit.SECONDS.sleep(2);
        // 此时该队列的数据元素为(队尾)Alex->Java->world->hello
        // 从队列头部移除元素hello，但是匿名线程仍然阻塞
        System.out.println(queue.take());
        // 在队尾插入新元素，此时该队列的数据元素为(队尾)Scala->Alex->Java->world
        queue.put("Scala");
        // 从队列头部移除元素world，但是匿名线程仍然阻塞，此时该队列的数据元素为(队尾)Scala->Alex->Java
        System.out.println(queue.poll());
        TimeUnit.SECONDS.sleep(2);
        // 从队列头部移除元素Java，但是匿名线程仍然阻塞，此时该队列的数据元素为(队尾)Scala->Alex
        System.out.println(queue.take());
        // 从队列头部移除元素Alex，匿名线程退出阻塞
        System.out.println(queue.take());
    }
}
