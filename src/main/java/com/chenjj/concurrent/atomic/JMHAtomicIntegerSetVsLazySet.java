package com.chenjj.concurrent.atomic;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ▪ void set(int newValue)：为AtomicInteger的value设置一个新值，通过对前面内容的学习，
 * 我们知道在AtomicInteger中有一个被volatile关键字修饰的value成员属性，
 * 因此调用set方法为value设置新值后其他线程就会立即看见。
 * ▪ void lazySet(int newValue)：set方法修改被volatile关键字修饰的value值会被强制刷新到主内存中，
 * 从而立即被其他线程看到，这一切都应该归功于volatile关键字底层的内存屏障。
 * 内存屏障虽然足够轻量，但是毕竟还是会带来性能上的开销，
 * 比如，在单线程中对AtomicInteger的value进行修改时没有必要保留内存屏障，
 * 而value又是被volatile关键字修饰的，这似乎是无法调和的矛盾。
 * 幸好追求性能极致的JVM开发者们早就考虑到了这一点，lazySet方法的作用正在于此(不要内存屏障)。
 * Benchmark                                 Mode  Cnt  Score    Error  Units
 * JMHAtomicIntegerSetVsLazySet.testLazySet  avgt   10  0.001 ±  0.001  us/op
 * JMHAtomicIntegerSetVsLazySet.testSet      avgt   10  0.007 ±  0.001  us/op
 * 说明内存屏障是有开销的。
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHAtomicIntegerSetVsLazySet {
    private AtomicInteger atomicInteger;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHAtomicIntegerSetVsLazySet.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }

    @Setup
    public void setUp() {
        this.atomicInteger = new AtomicInteger(0);
    }

    @Benchmark
    public void testSet() {
        this.atomicInteger.set(10);
    }

    @Benchmark
    public void testLazySet() {
        this.atomicInteger.lazySet(10);
    }
}
