package com.chenjj.concurrent.jmh.compilerControl;

import com.chenjj.concurrent.jmh.param.JMHParam1;
import org.openjdk.jmh.annotations.*;
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
public class JMHCompilerControl {
    public static void main(String[] args) throws RunnerException {
        // include必须是当前类的名称，如果是其它类名称，那么执行的将是其它类的基准测试
        final Options options = new OptionsBuilder().include(JMHCompilerControl.class.getSimpleName()).build();
        new Runner(options).run();
    }

    @Benchmark
    public void test1() {

    }

    // 禁止优化
    @CompilerControl(CompilerControl.Mode.EXCLUDE)
    @Benchmark
    public void test2() {
        Math.log(Math.PI);
    }
}
