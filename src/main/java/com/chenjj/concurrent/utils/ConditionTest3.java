package com.chenjj.concurrent.utils;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ConditionTest3 {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition FULL_CONDITION = lock.newCondition();
    private static final Condition EMPTY_CONDITION = lock.newCondition();
    private static final LinkedList<Long> list = new LinkedList<>();
    private static final int CAPACITY = 100;
    private static long i = 0;

    public static void main(String[] args) {
        // 10个生产者
        IntStream.range(0, 10).forEach(i -> {
            new Thread(() -> {
                for (; ; ) {
                    produce();
                    sleep();
                }
            }, "Producer-" + i).start();
        });

        // 5个消费者
        IntStream.range(0, 5).forEach(i -> {
            new Thread(() -> {
                for (; ; ) {
                    consume();
                    sleep();
                }
            }, "Consumer-" + i).start();
        });
    }

    /**
     * 生产者方法
     */
    private static void produce() {
        lock.lock();
        try {
            // 当list中的数据量达到100时，生产线程将会阻塞并加入到与condition关联的wait队列中
            while (list.size() >= CAPACITY) {
                FULL_CONDITION.await();
            }
            i++;
            list.addLast(i);
            System.out.println(Thread.currentThread().getName() + "->" + i);
            EMPTY_CONDITION.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private static void consume() {
        lock.lock();
        try {
            // 当list中的数据为空时，消费线程将会阻塞并加入到与condition关联的wait队列中
            while (list.isEmpty()) {
                EMPTY_CONDITION.await();
            }
            long value = list.removeFirst();
            System.out.println(Thread.currentThread().getName() + "->" + value);
            FULL_CONDITION.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private static void sleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
