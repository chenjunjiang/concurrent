package com.chenjj.concurrent.jmh.profiler;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.CompilerProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                                                  Mode  Cnt    Score   Error  Units
 * JMHCompilerProfiler.testLoadClass                          avgt    5    1.748 ± 0.144  us/op
 * JMHCompilerProfiler.testLoadClass:·compiler.time.profiled  avgt    5    7.000             ms
 * JMHCompilerProfiler.testLoadClass:·compiler.time.total     avgt    5  438.000             ms
 * 我们可以看到，在整个方法的执行过程中，profiled的优化耗时为7毫秒，total的优化耗时为438毫秒。
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHCompilerProfiler {
    private byte[] alexBytes;
    private AlexClassLoader classLoader;

    public static void main(String[] args) throws Exception {
        final Options options = new OptionsBuilder().include(JMHCompilerProfiler.class.getSimpleName())
                // 添加CompilerProfiler
                .addProfiler(CompilerProfiler.class)
                .verbosity(VerboseMode.EXTRA)
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
