package com.chenjj.concurrent.metrics.histogram;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 *20-11-17 18:51:32 ==============================================================
 *
 * -- Histograms ------------------------------------------------------------------
 * SlidingTimeWindowReservoir-Histogram
 *              count = 5
 *                min = 2
 *                max = 8
 *               mean = 5.60
 *             stddev = 2.61
 *             median = 6.00
 *               75% <= 8.00
 *               95% <= 8.00
 *               98% <= 8.00
 *               99% <= 8.00
 *             99.9% <= 8.00
 *
 * 在定义SlidingTimeWindowReservoir时间滑动窗口的时候，我们需要指定该窗口的时间大小，比如代码中的30秒，
 * 这就意味着最多将针对最近30秒以内的度量数据进行中间值的计算。
 */
public class SlidingTimeWindowReservoirTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    // 采用SlidingTimeWindowReservoir定义Histogram，并时间窗口为30s
    private static final Histogram histogram = new Histogram(new SlidingTimeWindowReservoir(30, TimeUnit.SECONDS));

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);
        registry.register("SlidingTimeWindowReservoir-Histogram", histogram);

        while (true) {
            doSearch();
            randomSleep();
        }
    }

    private static void doSearch() {
        // 搜索结果从随机数中获得0~9之间的结果条目
        histogram.update(ThreadLocalRandom.current().nextInt(10));
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
