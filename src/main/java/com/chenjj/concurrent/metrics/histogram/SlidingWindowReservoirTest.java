package com.chenjj.concurrent.metrics.histogram;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-17 18:47:33 ==============================================================
 *
 * -- Histograms ------------------------------------------------------------------
 * SlidingWindowReservoir-Histogram
 *              count = 5
 *                min = 0
 *                max = 9
 *               mean = 3.60
 *             stddev = 3.36
 *             median = 3.00
 *               75% <= 6.50
 *               95% <= 9.00
 *               98% <= 9.00
 *               99% <= 9.00
 *             99.9% <= 9.00
 *
 * 在定义SlidingWindowReservoir滑动窗口时，我们需要指定该窗口的大小，比如上述代码中的50，这就意味着最多
 * 将针对最近的50个度量数据进行中间值的计算。
 */
public class SlidingWindowReservoirTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    // 采用SlidingWindowReservoir定义Histogram，并指定窗口大小为50
    private static final Histogram histogram = new Histogram(new SlidingWindowReservoir(50));

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);
        registry.register("SlidingWindowReservoir-Histogram", histogram);

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
