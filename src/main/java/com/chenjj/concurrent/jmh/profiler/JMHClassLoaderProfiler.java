package com.chenjj.concurrent.jmh.profiler;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.ClassloaderProfiler;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(1)
// 将Warmup设置为0，以避免在热身阶段就已经加载了基准测试方法所需的所有类。
@Warmup(iterations = 0)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHClassLoaderProfiler {
    private byte[] alexBytes;
    private AlexClassLoader classLoader;

    public static void main(String[] args) throws Exception {
        final Options options = new OptionsBuilder().include(JMHClassLoaderProfiler.class.getSimpleName())
                // 添加ClassloaderProfiler，输出类加载、卸载信息
                .addProfiler(ClassloaderProfiler.class)
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
