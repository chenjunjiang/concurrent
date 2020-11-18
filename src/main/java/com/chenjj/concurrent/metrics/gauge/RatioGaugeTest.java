package com.chenjj.concurrent.metrics.gauge;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * RROR.
 * 20-11-17 15:49:01 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * success-rate
 *              value = 0.8333333333333334
 *
 * totalMeter和successMeter完全可以使用AtomicLong替代，这没有任何问题，目的都主要是对数值进行存储。
 */
public class RatioGaugeTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    private static final Meter totalMeter = new Meter();
    private static final Meter successMeter = new Meter();

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);
        registry.register("success-rate", new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(successMeter.getCount(), totalMeter.getCount());
            }
        });

        // 模拟服务
        for (; ; ) {
            shortSleep();
            business();
        }
    }

    private static void business() {
        // 无论正确与否，totalMeter都会增加
        totalMeter.mark();
        try {
            // 随机数可能是0，因此这个操作可能会出现错误
            int x = 10 / ThreadLocalRandom.current().nextInt(6);
            //成功之后，success会增加
            successMeter.mark();
        } catch (Exception e) {
            System.out.println("ERROR.");
        }
    }

    private static void shortSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(6));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
