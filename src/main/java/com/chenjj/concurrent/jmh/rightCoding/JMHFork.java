package com.chenjj.concurrent.jmh.rightCoding;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark                          Mode  Cnt  Score   Error  Units
 * rightCoding.JMHFork.measure_inc_1  avgt    5  0.004 ± 0.002  us/op
 * rightCoding.JMHFork.measure_inc_2  avgt    5  0.020 ± 0.002  us/op
 * rightCoding.JMHFork.measure_inc_3  avgt    5  0.022 ± 0.006  us/op
 * 将Fork设置为0，每一个基准测试方法都将会与JMHFork使用同一个JVM进程，因此基准测试方法可能会混入JMHFork进程的Profiler。
 * measure_inc_1和measure_inc_2的实现方式几乎是一致的，它们的性能却存在着较大的差距，
 * 虽然measure_inc_1和measure_inc_3的代码实现完全相同，但还是存在着不同的性能数据，
 * 这其实就是JVM Profiler-guidedoptimizations导致的，由于我们所有的基准测试方法都与JMHFork的JVM进程共享，
 * 因此难免在其中混入JMHFork进程的Profiler，但是在将Fork设置为1的时候，
 * 也就是说每一次运行基准测试方法时都会开辟一个全新的JVM进程对其进行测试，那么多个基准测试之间将不会再存在干扰。
 * 当然，你可以将Fork设置为大于1的数值，那么它将多次运行在不同的进程中，不过一般情况下，我们只需要将Fork设置为1即可。
 * 如果Fork设置为1，通过jps命令观察，确实每个基准测试方法的调用都是在一个新的JVM进程中。
 */
@BenchmarkMode(Mode.AverageTime)
// 将Fork设置为0
@Fork(0)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHFork {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHFork.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    interface Inc {
        int inc();
    }

    public static class Inc1 implements Inc {
        private int i = 0;

        @Override
        public int inc() {
            return ++i;
        }
    }

    public static class Inc2 implements Inc {
        private int i = 0;

        @Override
        public int inc() {
            return ++i;
        }
    }

    private Inc inc1 = new Inc1();
    private Inc inc2 = new Inc2();

    @Benchmark
    public int measure_inc_1() {
        return this.measure(inc1);
    }

    @Benchmark
    public int measure_inc_2() {
        return this.measure(inc2);
    }

    @Benchmark
    public int measure_inc_3() {
        return this.measure(inc1);
    }

    private int measure(Inc inc) {
        int result = 0;
        for (int i = 0; i < 10; i++) {
            result += inc.inc();
        }
        return result;
    }

}
