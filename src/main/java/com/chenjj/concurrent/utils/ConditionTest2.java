package com.chenjj.concurrent.utils;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * 下面的程序虽然能够正常运行，但是仍然存在一些不足之处，比如在condition.signalAll()处，
 * 此刻唤醒的是与Condition关联的阻塞队列中的所有阻塞线程。由于我们使用的是唯一的一个Condition实例，
 * 因此生产者唤醒的有可能是与Condition关联的wait队列中的生产者线程，假设当生产者线程被唤醒后抢到了CPU的调度获得执行权，
 * 但是又发现队列已满再次进入阻塞。这样的线程上下文开销实际上是没有意义的，
 * 甚至会影响性能（多线程下的线程上下文开销其实是一个非常大的性能损耗，
 * 一般针对高并发程序的调优就是在减少上下文切换发生的概率）。
 * 那么我们应该如何进行优化呢？使用两个Condition对象，一个用于对队列已满临界值条件的处理，另外一个用于对队列为空的临界值条件的处理，
 * 这样一来，在生产者中唤醒的阻塞线程只能是消费者线程，在消费者中唤醒的也只能是生产者线程。见ConditionTest3
 * 使用传统的对象监视器（Object Monitor）的方式是很难优雅地解决这个问题的。
 */
public class ConditionTest2 {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
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
                condition.await();
            }
            i++;
            list.addLast(i);
            System.out.println(Thread.currentThread().getName() + "->" + i);
            condition.signalAll();
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
                condition.await();
            }
            long value = list.removeFirst();
            System.out.println(Thread.currentThread().getName() + "->" + value);
            condition.signalAll();
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
