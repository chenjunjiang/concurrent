package com.chenjj.concurrent.metrics.timer;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-17 19:21:16 ==============================================================
 *
 * -- Timers ----------------------------------------------------------------------
 * request
 *              count = 3
 *          mean rate = 0.30 calls/second
 *      1-minute rate = 0.03 calls/second
 *      5-minute rate = 0.01 calls/second
 *     15-minute rate = 0.00 calls/second
 *                min = 0.00 seconds
 *                max = 8.00 seconds
 *               mean = 3.32 seconds
 *             stddev = 3.39 seconds
 *             median = 2.00 seconds
 *               75% <= 8.00 seconds
 *               95% <= 8.00 seconds
 *               98% <= 8.00 seconds
 *               99% <= 8.00 seconds
 *             99.9% <= 8.00 seconds
 */
public class TimerTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    private static final Timer timer = registry.timer("request");

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);
        // 模拟服务调用
        while (true){
            business();
        }
    }

    /**
     * 业务处理方法
     */
    private static void business() {
        // 在方法体中定义Timer上下文
        Timer.Context context = timer.time();
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 方法执行结束之后stop timer上下文
            context.stop();
        }
    }
}
