package com.chenjj.concurrent.jmh.rightCoding;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark                                      Mode  Cnt   Score   Error  Units
 * rightCoding.JMHConstantFold.returnCalculate_1  avgt    5   2.803 ± 0.187  ns/op
 * rightCoding.JMHConstantFold.returnCalculate_2  avgt    5  44.574 ± 1.849  ns/op
 * rightCoding.JMHConstantFold.returnCalculate_3  avgt    5   2.836 ± 0.276  ns/op
 * rightCoding.JMHConstantFold.returnDirect       avgt    5   2.862 ± 0.349  ns/op
 * 我们可以看到，1、3、4三个方法的统计数据几乎相差无几，这也就意味着在编译器优化的时候发生了常量折叠，
 * 这些方法在运行阶段根本不需要再进行计算，直接将结果返回即可，而第二个方法的统计数据就没那么好看了，
 * 因为早期的编译阶段不会对其进行任何的优化。
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class JMHConstantFold {
    private final double x1 = 124.56;
    private final double x2 = 342.56;

    private double y1 = 124.56;
    private double y2 = 342.56;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHConstantFold.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public double returnDirect() {
        return 42_620.703936d;
    }

    @Benchmark
    public double returnCalculate_1() {
        return x1 * x2;
    }

    @Benchmark
    public double returnCalculate_2() {
        return Math.log(y1) * Math.log(y2);
    }

    @Benchmark
    public double returnCalculate_3() {
        return Math.log(x1) * Math.log(x2);
    }
}
