package com.chenjj.concurrent.jmh.profiler;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                                                     Mode  Cnt      Score     Error   Units
 * JMHGCProfiler.testLoadClass                                   avgt    5      2.016 ±   0.232   us/op
 * JMHGCProfiler.testLoadClass:·gc.alloc.rate                    avgt    5   3245.290 ± 365.000  MB/sec
 * JMHGCProfiler.testLoadClass:·gc.alloc.rate.norm               avgt    5  10280.001 ±   0.001    B/op
 * JMHGCProfiler.testLoadClass:·gc.churn.PS_Eden_Space           avgt    5   3275.382 ± 347.120  MB/sec
 * JMHGCProfiler.testLoadClass:·gc.churn.PS_Eden_Space.norm      avgt    5  10375.743 ± 103.433    B/op
 * JMHGCProfiler.testLoadClass:·gc.churn.PS_Survivor_Space       avgt    5      0.594 ±   0.416  MB/sec
 * JMHGCProfiler.testLoadClass:·gc.churn.PS_Survivor_Space.norm  avgt    5      1.882 ±   1.326    B/op
 * JMHGCProfiler.testLoadClass:·gc.count                         avgt    5    594.000            counts
 * JMHGCProfiler.testLoadClass:·gc.time                          avgt    5    307.000                ms
 * 根据GcProfiler的输出信息可以看到，在这个基准方法执行的过程之中，gc总共出现过594次，
 * 这594次总共耗时307毫秒，在此期间也发生了多次的堆内存的申请，
 * 比如，每秒钟大约会有3245.290MB的数据被创建，若换算成对testLoadClass方法的每次调用，
 * 那么我们会发现大约有6860.31 Byte的内存使用。
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHGCProfiler {
    private byte[] alexBytes;
    private AlexClassLoader classLoader;

    public static void main(String[] args) throws Exception {
        final Options options = new OptionsBuilder().include(JMHGCProfiler.class.getSimpleName())
                // 添加GCProfiler
                .addProfiler(GCProfiler.class)
                .jvmArgs("-Xms128M", "-Xmx128M")
                .build();
        new Runner(options).run();
    }

    @Setup
    public void init() throws IOException {
        this.alexBytes = Files.readAllBytes(Paths.get("D:\\classloader\\com\\chenjj\\concurrent\\jmh\\profiler\\Alex.class"));
        this.classLoader = new AlexClassLoader(alexBytes);
    }

    @Benchmark
    public Object testLoadClass() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> alexClass = Class.forName("com.chenjj.concurrent.jmh.profiler.Alex", true, classLoader);
        return alexClass.newInstance();
    }
}
