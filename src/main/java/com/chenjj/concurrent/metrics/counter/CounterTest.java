package com.chenjj.concurrent.metrics.counter;

import com.chenjj.concurrent.metrics.gauge.SimpleGaugeTest;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * -- Counters --------------------------------------------------------------------
 * queue-count
 *              count = 0
 *
 *
 * 20-11-17 17:29:54 ==============================================================
 *
 * -- Counters --------------------------------------------------------------------
 * queue-count
 *              count = 2
 *
 *
 * 20-11-17 17:29:55 ==============================================================
 *
 * -- Counters --------------------------------------------------------------------
 * queue-count
 *              count = 3
 *
 *
 * 20-11-17 17:29:56 ==============================================================
 *
 * -- Counters --------------------------------------------------------------------
 * queue-count
 *              count = 2
 */
public class CounterTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    // 定义一个双向队列，这个队列是需要被监控的
    private static final BlockingQueue<Long> queue = new LinkedBlockingDeque<>(1_000);

    public static void main(String[] args) {
        // 每10秒将会对Registry中的所有Metric进行一次report。
        reporter.start(1, TimeUnit.SECONDS);
        Counter counter = registry.counter("queue-count");

        // 不断向队列放入数据
        new Thread(() -> {
            for (; ; ) {
                randomSleep();
                queue.add(System.nanoTime());
                counter.inc();
            }
        }).start();

        // 不断向队列poll数据
        new Thread(() -> {
            for (; ; ) {
                randomSleep();
                if (queue.poll() != null) {
                    counter.dec();
                }
            }
        }).start();
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
