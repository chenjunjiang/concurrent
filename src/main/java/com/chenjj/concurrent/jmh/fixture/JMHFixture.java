package com.chenjj.concurrent.jmh.fixture;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHFixture {
    private List<String> list;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHFixture.class.getSimpleName())
                // 激活断言，enable assertion的意思，否则下边的assert不会执行；通过这种方式设置的jvm参数会导致在idea configurations中设置的失效
                .jvmArgs("-ea", "-Xms1024m", "-Xmx1024m")
                .build();
        new Runner(options).run();
    }

    @Setup
    public void setUp() {
        this.list = new ArrayList<>();
    }

    @Benchmark
    public void measureRight() {
        this.list.add("Test");
    }

    @Benchmark
    public void measureWrong() {

    }

    @TearDown
    public void tearDown() {
        assert this.list.size() > 0 : "The list elements must greater than zero";
    }

}
