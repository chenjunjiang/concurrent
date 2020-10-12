package com.chenjj.concurrent.jmh.param;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
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
public class JMHParam2 {
    // 为type提供四种可配置的参数值
    @Param({"1", "2", "3", "4"})
    private int type;
    private Map<Long, Long> map;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHParam2.class.getSimpleName()).build();
        new Runner(options).run();
    }

    @Setup
    public void setUp() {
        System.out.println("setUp被调用了......");
        switch (type) {
            case 1:
                this.map = new ConcurrentHashMap<>();
                break;
            case 2:
                this.map = new ConcurrentSkipListMap<>();
                break;
            case 3:
                this.map = new Hashtable<>();
                break;
            case 4:
                this.map = Collections.synchronizedMap(new HashMap<>());
                break;
            default:
                throw new IllegalArgumentException("Illegal map type.");
        }
    }

    /**
     * JMH会根据@Param所提供的参数值，对test方法进行基准测试的运行与统计，这里会分别调用4次setUp方法和test方法
     * 这样我们就不需要为每一个map容器都写一个基准测试方法了。
     */
    @Benchmark
    public void test() {
        this.map.put(System.nanoTime(), System.nanoTime());
    }
}
