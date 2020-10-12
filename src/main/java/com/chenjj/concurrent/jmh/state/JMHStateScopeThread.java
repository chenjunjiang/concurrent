package com.chenjj.concurrent.jmh.state;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
// 设置5个线程运行基准测试方法
@Threads(5)
public class JMHStateScopeThread {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHStateScopeThread.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    // 运行的5个线程，每个线程都持有一个Test的实例
    @State(Scope.Thread)
    public static class Test {
        public Test() {
            System.out.println("create instance");
        }

        public void method() {

        }
    }

    @Benchmark
    public void test(Test test) {
        test.method();
    }
}
