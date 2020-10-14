package com.chenjj.concurrent.jmh.rightCoding;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHBlackHole {
    double x1 = Math.PI;
    double x2 = Math.PI * 2;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHBlackHole.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public double baseline() {
        // 不是Dead Code， 因为对结果进行了返回
        return Math.pow(x1, 2);
    }

    @Benchmark
    public double powButReturnOne() {
        // Dead Code会被擦除
        Math.pow(x1, 2);
        // 不是Dead Code， 因为对结果进行了返回
        return Math.pow(x1, 2);
    }

    @Benchmark
    public double powThenAdd() {
        // 通过加运算对两个结果进行了合并然后返回，因此两次的计算都会生效
        return Math.pow(x1, 2) + Math.pow(x1, 2);
    }

    @Benchmark
    public void powThenAdd1(Blackhole blackhole) {
        blackhole.consume(Math.pow(x1, 2) + Math.pow(x1, 2));
    }

    @Benchmark
    public void useBlackHole(Blackhole blackhole) {
        // 将结果放入Blackhole
        blackhole.consume(Math.pow(x1, 2));
        blackhole.consume(Math.pow(x2, 2));
    }

}
