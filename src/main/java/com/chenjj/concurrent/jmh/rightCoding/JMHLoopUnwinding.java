package com.chenjj.concurrent.jmh.rightCoding;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark                                      Mode  Cnt  Score   Error  Units
 * rightCoding.JMHLoopUnwinding.measure           avgt   10  2.805 ± 0.304  ns/op
 * rightCoding.JMHLoopUnwinding.measureLoop_1     avgt   10  2.860 ± 0.278  ns/op
 * rightCoding.JMHLoopUnwinding.measureLoop_10    avgt   10  0.314 ± 0.023  ns/op
 * rightCoding.JMHLoopUnwinding.measureLoop_100   avgt   10  0.036 ± 0.001  ns/op
 * rightCoding.JMHLoopUnwinding.measureLoop_1000  avgt   10  0.040 ± 0.009  ns/op
 * 通过JMH的基准测试我们不难发现，在循环次数多的情况下，折叠的情况也比较多，因此性能会比较好，说明JVM在运行期对我们的代码进行了优化。
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class JMHLoopUnwinding {
    private int x = 1;
    private int y = 1;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHLoopUnwinding.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public int measure() {
        return (x + y);
    }

    @OperationsPerInvocation(1)
    @Benchmark
    public int measureLoop_1() {
        return loopCompute(1);
    }

    /**
     * 在上面的代码中，measure()方法进行了x+y的计算，measureLoop_1()方法与measure()方法几乎是等价的，
     * 也是进行了x+y的计算，但是measureLoop_10()方法对result+=（x+y）进行了10次这样的操作，
     * 其实说白了就是调用了10次measure()或者loopCompute（times=1）。
     * 但是我们肯定不能直接拿10次的运算和1次运算所耗费的CPU时间去做比较，
     * 因此@OperationsPerInvocation（10）注解的作用就是在每一次对measureLoop_10()方法进行基准调用的时候将op操作记为10次。
     * JMH统计结果的时候是时间/op，就会除以10，得到每次的时间，和measureLoop_1()就有可比性了。
     */
    @OperationsPerInvocation(10)
    @Benchmark
    public int measureLoop_10() {
        return loopCompute(10);
    }

    @OperationsPerInvocation(100)
    @Benchmark
    public int measureLoop_100() {
        return loopCompute(100);
    }

    @OperationsPerInvocation(1000)
    @Benchmark
    public int measureLoop_1000() {
        return loopCompute(1000);
    }

    private int loopCompute(int times) {
        int result = 0;
        for (int i = 0; i < times; i++) {
            result += (x + y);
        }
        return result;
    }
}
