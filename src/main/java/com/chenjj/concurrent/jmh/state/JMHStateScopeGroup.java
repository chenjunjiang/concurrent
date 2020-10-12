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
public class JMHStateScopeGroup {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHStateScopeGroup.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    // 线程共享
    @State(Scope.Group)
    public static class Test {
        public Test() {
            System.out.println("create instance");
        }

        public void write() {
            System.out.println("write");
        }

        public void read() {
            System.out.println("read");
        }
    }

    /**
     * 在线程组test中，有三个线程不断地对Test实例的write方法进行调用
     *
     * @param test
     */
    @GroupThreads(3)
    @Group("test")
    @Benchmark
    public void testWrite(Test test) {
        test.write();
    }

    /**
     * 在线程组test中，有三个线程不断地对Test实例的read方法进行调用
     *
     * @param test
     */
    @GroupThreads(3)
    @Group("test")
    @Benchmark
    public void testRead(Test test) {
        test.read();
    }
}
