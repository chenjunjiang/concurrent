package com.chenjj.concurrent.metrics.histogram;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-17 17:40:26 ==============================================================
 *
 * -- Histograms ------------------------------------------------------------------
 * search-result
 *              count = 10
 *                min = 2
 *                max = 9
 *               mean = 5.32
 *             stddev = 2.39
 *             median = 5.00
 *               75% <= 7.00
 *               95% <= 9.00
 *               98% <= 9.00
 *               99% <= 9.00
 *             99.9% <= 9.00
 *运行下面的程序，你会发现具有非常多的度量数据统计信息，这些信息对我们的帮助是非常大的，它可以很准确地告诉我们
 * 这些数据的分布情况，比如，有75%的搜索结果都少于7个条目
 * 除了要对数据区间进行统计之外，还有些数据也是非常重要的：
 * ▪ count：参与统计的数据有多少条。
 * ▪ min：在所有统计数据中哪个值是最小的。
 * ▪ max：在所有统计数据中哪个值是最大的。
 * ▪ mean：所有数据的平均值。
 * ▪ stddev：统计结果的标准误差率。
 * ▪ median：所有统计数据的中间值。
 * 上述几项统计结果中，除了median（中间值）之外，其他的都比较容易计算。传统的计算中间值的方式是将所有的数据都放到
 * 一个数据集合中，然后对其进行排序整理，最后采取折半的方式获取其中的一个值，就认为其是中间值。但是，这种计算中间值
 * 的前提是需要在数据集合中记录所有的数据，这会导致我们的应用程序存放大量的度量数据，进而带来应用程序发生内存溢出
 * 的风险，显然这种方式是不可取的。Metrics在设计的过程中也充分考虑到了这一点，因此为我们提供了4种解决方案来解决
 * 这样的问题：
 * 1. Uniform Reservoirs
 * Uniform Reservoirs采用随机的抽样来度量数据，然后存放在一个数据集合中进行中间值的统计，这种方法被称为Vitter R算法
 * 详见（http://www.cs.umd.edu/~samir/498/vitter.pdf）。例子见UniformReservoirHistogramTest
 *
 * 2. Exponentially Decaying Reservoirs
 * Exponentially Decaying Reservoirs（指数衰变）的方式既是Metrics的默认方式，也是官网推荐的一种方式，
 * 建议在平时的工作中使用这种方式即可。Exponentially Decaying Reservoirs通过一个正向衰减优先级列表来实现，
 * 该列表用于更新维护数据的指数权重，使得需要计算中间值的数据集合维持在一个特定的数量区间中，然后对其进行取中值运算。
 *
 * 3. Sliding Window Reservoirs
 * Sliding Window Reservoirs（滑动窗口）的原理非常简单，主要是在该窗口中存放最近的一定数量的
 * 值进行median（中间值）的计算。见SlidingWindowReservoirTest
 *
 * 4. Sliding Time Window Reservoirs
 * Sliding Time Window Reservoirs（时间滑动窗口）的原理也是非常简单的，主要是根据指定的、最近的时间范围内的数据
 * 进行median（中间值）的计算。见SlidingTimeWindowReservoirTest
 *
 */
public class HistogramTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    private static final Histogram histogram = registry.histogram("search-result");

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);

        while (true){
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
