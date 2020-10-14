package com.chenjj.concurrent.jmh.rightCoding;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 运行结果：
 * Benchmark                            Mode  Cnt   Score    Error  Units
 * rightCoding.JMHDeadCode.baseline     avgt    5  ≈ 10⁻³           us/op
 * rightCoding.JMHDeadCode.measureLog1  avgt    5  ≈ 10⁻³           us/op
 * rightCoding.JMHDeadCode.measureLog2  avgt    5  ≈ 10⁻³           us/op
 * rightCoding.JMHDeadCode.measureLog3  avgt    5   0.003 ±  0.001  us/op
 * 从输出结果可以看出，measureLog1和measureLog2方法的基准性能与baseline几乎完全一致，
 * 因此我们可以肯定的是，这两个方法中的代码进行过擦除操作，
 * 这样的代码被称为Dead Code（死代码，其他地方都没有用到的代码片段），
 * 而measureLog3则与上述两个方法不同，由于它对结果进行了返回，
 * 因此Math.log(PI)不会被认为它是Dead Code，因此它将占用一定的CPU时间。
 * <p>
 * 通过这个例子我们可以发现，若想要编写性能良好的微基准测试方法，则不要让方法存在Dead Code，最好每一个基准测试方法都有返回值。
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHDeadCode {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHDeadCode.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public void baseline() {

    }

    @Benchmark
    public void measureLog1() {
        Math.log(Math.PI);
    }

    @Benchmark
    public void measureLog2() {
        // result是通过运算所得并且在下一行代码中得到了使用
        double result = Math.log(Math.PI);
        // 对result进行运算，但是结果既不保存也不返回
        Math.log(result);
        // 输出到控制台就不是Dead Code了
        // System.out.println(result);
    }

    @Benchmark
    public double measureLog3() {
        // 返回运算结果
        return Math.log(Math.PI);
    }
}
