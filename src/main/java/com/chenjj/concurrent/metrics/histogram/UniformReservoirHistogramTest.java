package com.chenjj.concurrent.metrics.histogram;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.UniformReservoir;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-17 18:40:14 ==============================================================
 *
 * -- Histograms ------------------------------------------------------------------
 * UniformReservoir-Histogram
 *              count = 4
 *                min = 0
 *                max = 8
 *               mean = 5.25
 *             stddev = 3.59
 *             median = 6.50
 *               75% <= 7.75
 *               95% <= 8.00
 *               98% <= 8.00
 *               99% <= 8.00
 *             99.9% <= 8.00
 *
 * 运行下面的程序，虽然输出结果与HistogramTest.java一样，但是其中对median的计算方式却是不同的。
 * 需要注意的是，这种方式非常适合于统计长时间运行的度量数据，千万不要用它来度量只需要关心最近一段时间的统计结果，
 * 因为它是采用随机抽样的方式为数据集合提供统计原料的。
 */
public class UniformReservoirHistogramTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    // 使用UniformReservoir构造Histogram
    private static final Histogram histogram = new Histogram(new UniformReservoir());

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);
        registry.register("UniformReservoir-Histogram", histogram);

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
