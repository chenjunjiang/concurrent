package com.chenjj.concurrent.jmh.rightCoding;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 我们在对AtomicInteger进行自增操作的同时又会对其进行读取操作，
 * 这就是我们经常见到的高并发环境中某些API的操作方式，同样也是线程安全存在隐患的地方
 * Benchmark                        Mode  Cnt  Score   Error  Units
 * rightCoding.JMHAsymmetric.q      avgt    5  0.036 ± 0.039  us/op
 * rightCoding.JMHAsymmetric.q:get  avgt    5  0.002 ± 0.002  us/op
 * rightCoding.JMHAsymmetric.q:inc  avgt    5  0.070 ± 0.077  us/op
 * 5个读线程，5个写线程同时操作变量的平均响应时间
 * 5个读线程同时读取变量的平均响应时间
 * 5个写线程同时修改变量的平均响应时间
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Group)
public class JMHAsymmetric {
    private AtomicInteger counter;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHAsymmetric.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Setup
    public void init() {
        this.counter = new AtomicInteger();
    }

    @GroupThreads(5)
    @Group("q")
    @Benchmark
    public void inc() {
        this.counter.incrementAndGet();
    }

    @GroupThreads(5)
    @Group("q")
    @Benchmark
    public void get() {
        this.counter.get();
    }
}
