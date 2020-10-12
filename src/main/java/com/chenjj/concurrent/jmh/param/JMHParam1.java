package com.chenjj.concurrent.jmh.param;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * -Xms512m -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:/jmh.hprof
 * 运行之后会报：
 * java.lang.OutOfMemoryError: GC overhead limit exceeded
 * 内存溢出，JVM内存被耗尽
 * 可以加大JVM内存后再进行测试
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Threads(5)
// 设置为线程之间共享的资源，各个线程共享一个JMHParam1实例
@State(Scope.Benchmark)
public class JMHParam1 {
    private Map<Long, Long> concurrentMap;
    private Map<Long, Long> synchronizedMap;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHParam1.class.getSimpleName()).build();
        new Runner(options).run();
    }

    @Setup
    public void setUp() {
        concurrentMap = new ConcurrentHashMap<>();
        synchronizedMap = Collections.synchronizedMap(new HashMap<>());
    }

    @Benchmark
    public void testConcurrentMap() {
        this.concurrentMap.put(System.nanoTime(), System.nanoTime());
    }

    @Benchmark
    public void testSynchronizedMap() {
        this.synchronizedMap.put(System.nanoTime(), System.nanoTime());
    }
}
