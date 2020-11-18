package com.chenjj.concurrent.metrics.gauge;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-17 14:27:14 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * com.chenjj.concurrent.metrics.gauge.SimpleGaugeTest.queue-size
 *              value = 2
 *
 * 通过输出信息，可以看到queue size的变化。通过这个度量数据，我们很容易就能发现生产者线程和消费者线程的处理速度，
 * 以及队列出现的积压情况，这对我们分析工作线程的运行性能非常重要。
 */
public class SimpleGaugeTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    // 定义一个双向队列，这个队列是需要被监控的
    private static final BlockingQueue<Long> queue = new LinkedBlockingDeque<>(1_000);

    public static void main(String[] args) {
        // 定义一个Simple Gauge并注册到registry中，Gauge的实现仅仅是返回queue size
        registry.register(MetricRegistry.name(SimpleGaugeTest.class, "queue-size"), (Gauge<Integer>) queue::size);

        // 每10秒将会对Registry中的所有Metric进行一次report。
        reporter.start(1, TimeUnit.SECONDS);

        // 不断向队列放入数据
        new Thread(() -> {
            for (; ; ) {
                randomSleep();
                queue.add(System.nanoTime());
            }
        }).start();

        // 不断向队列poll数据
        new Thread(() -> {
            for (; ; ) {
                randomSleep();
                queue.poll();
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
