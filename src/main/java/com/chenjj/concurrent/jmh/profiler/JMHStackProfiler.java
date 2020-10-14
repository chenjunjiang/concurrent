package com.chenjj.concurrent.jmh.profiler;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 观察输出结果会发现当出现阻塞时，jmh最多等待指定的超时时间会继续执行而不是像之前那样陷入长时间的阻塞。
 * 阻塞所耗费的时间并不会纳入统计。
 * <p>
 * ....[Thread state distributions]....................................................................
 * 87.5%         WAITING
 * 12.5%         RUNNABLE
 * <p>
 * 输出结果可以看到，线程状态的分布情况为WAITING：87.5%，RUNNABLE:12.5%，考虑到我们使用的是BlockingQueue，因此这种分布应该还算合理。
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Group)
public class JMHStackProfiler {
    private BlockingQueue<Integer> queue;

    private final static int VALUE = Integer.MAX_VALUE;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHStackProfiler.class.getSimpleName())
                // 将每批次的超时时间设置为10秒
                .timeout(TimeValue.seconds(10))
                // 添加StackProfiler
                .addProfiler(StackProfiler.class)
                .build();
        new Runner(options).run();
    }

    @Setup
    public void init() {
        this.queue = new ArrayBlockingQueue<>(10);
    }

    @GroupThreads(5)
    @Group("blockingQueue")
    @Benchmark
    public void put() throws InterruptedException {
        this.queue.put(VALUE);
    }

    @GroupThreads(5)
    @Group("blockingQueue")
    @Benchmark
    public int take() throws InterruptedException {
        return this.queue.take();
    }
}
