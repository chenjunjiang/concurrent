package com.chenjj.concurrent.metrics.meter;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * -- Meters ----------------------------------------------------------------------
 * tqs
 *              count = 98
 *          mean rate = 12.95 events/minute
 *      1-minute rate = 12.61 events/minute
 *      5-minute rate = 12.59 events/minute
 *     15-minute rate = 12.31 events/minute
 * volume
 *              count = 47374
 *          mean rate = 6259.34 events/minute
 *      1-minute rate = 5627.70 events/minute
 *      5-minute rate = 5268.58 events/minute
 *     15-minute rate = 3981.77 events/minute
 *
 * 对upload()的调用一分钟的平均速率将是12.95次，每分钟上传文件的平均字节数是6259.34字节。
 */
public class MeterTest {
    /**
     * 定义一个MetricRegistry，它的作用就是一个Metric的注册表，其将所有的Metric注册在该表中，以方便
     * Reporter对其进行获取。
     */
    private final static MetricRegistry registry = new MetricRegistry();
    /**
     * 定义了一个用于度量TQS的Meter，通过registry创建meter，除了会创建出一个Metric之外，
     * 还会将创建好的Metric顺便注册到注册表中。
     */
    private final static Meter requestMeter = registry.meter("tqs");
    /**
     * 定义了一个用于度量VOLUME的Meter。
     */
    private final static Meter sizeMeter = registry.meter("volume");

    public static void main(String[] args) {
        /**
         * 定义了一个ConsoleReporter，并且指定了将从哪个registry中获取Metric的度量数据。
         */
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES).build();
        // 启动Reporter，每隔10秒的时间将会对Registry中的所有Metric进行一次report。
        reporter.start(10, TimeUnit.SECONDS);
        // 模拟程序提供了不间断的服务
        for (; ; ) {
            // 上传数据
            upload(new byte[ThreadLocalRandom.current().nextInt(1000)]);
            randomSleep();
        }
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void upload(byte[] request) {
        // 对upload方法的每一次调用都会对tqs meter进行一次mark，也就意味着对其进行了一次计数。
        requestMeter.mark();
        // 对upload方法的每一次调用，都会通过volume meter对上传上来的字节流进行计数，以用于度量吞吐量。
        sizeMeter.mark(request.length);
    }
}
