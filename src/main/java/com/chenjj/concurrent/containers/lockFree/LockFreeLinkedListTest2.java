package com.chenjj.concurrent.containers.lockFree;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程环境下测试
 * 线程安全性的测试需要特别注意以下几点：
 * 1）在多线程中进行链表操作时会不会出现死锁；
 * 2）在多线程中进行链表操作时会不会出现数据不一致的问题；
 * 3）对数据结构的使用要有删有增。设计这样的测试方案会稍微有些难度，我们需要借助于其他的工具类来完成
 */
public class LockFreeLinkedListTest2 {
    public static void main(String[] args) throws InterruptedException {
        for (int iteration = 0; iteration < 100; iteration++) {
            LockFreeLinkedList<Integer> list = new LockFreeLinkedList<>();
            // 主要用于接下来的数据验证
            final ConcurrentSkipListSet set = new ConcurrentSkipListSet();
            // 用于生成LockFreeLinkedList将要存放的数据
            final AtomicInteger factory = new AtomicInteger();
            final AtomicInteger deleteCount = new AtomicInteger();
            // 启动10个线程对LockFreeLinkedList进行增删操作
            final CountDownLatch latch = new CountDownLatch(10);
            // 数据量为100万
            final int MAX_CAPACITY = 1_000_000;

            for (int i = 0; i < 10; i++) {
                new Thread(() -> {
                    while (true) {
                        int data = factory.getAndIncrement();
                        if (data < MAX_CAPACITY) {
                            list.add(data);
                            // 模拟随机删除
                            if (data % 2 == 0) {
                                list.removeFirst();
                                deleteCount.incrementAndGet();
                            }
                        } else {
                            break;
                        }
                    }
                    latch.countDown();
                }).start();
            }

            latch.await();

            assert list.count() == (MAX_CAPACITY - deleteCount.get());

            // 将所有数据放入set，主要看数据是否正确(set不允许重复)
            while (!list.isEmpty()) {
                set.add(list.removeFirst());
            }

            assert set.size() == (MAX_CAPACITY - deleteCount.get());

            System.out.printf("The iteration %d passed concurrent testing %n", iteration + 1);
        }
    }
}
