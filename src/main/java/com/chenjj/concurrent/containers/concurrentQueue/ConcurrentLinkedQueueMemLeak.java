package com.chenjj.concurrent.containers.concurrentQueue;

import com.google.common.base.Stopwatch;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * 经过试验发现，并没有发现队列的效率越来越低（慢）
 */
public class ConcurrentLinkedQueueMemLeak {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
        // 这句代码会导致内存泄露
        queue.add(new Object());
        Object object = new Object();

        int loops = 0;
        // 方便打开监控工具，监控执行前后的变化
        TimeUnit.SECONDS.sleep(10);

        Stopwatch stopwatch = Stopwatch.createStarted();
        while (true) {
            // 每执行10000次进行一次耗时统计
            if (loops % 10000 == 0 && loops != 0) {
                long elapsedMs = stopwatch.stop().elapsed(TimeUnit.MICROSECONDS);
                System.out.printf("loops=%d duration=%d MS%n", loops, elapsedMs);
                stopwatch.reset().start();
            }
            queue.add(object);
            queue.remove(object);
            ++loops;
        }
    }
}
