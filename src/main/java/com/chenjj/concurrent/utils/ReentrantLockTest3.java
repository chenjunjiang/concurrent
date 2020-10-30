package com.chenjj.concurrent.utils;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock VS. Synchronized
 * 单线程读操作性能对比
 * Benchmark                          Mode  Cnt  Score    Error  Units
 * ReentrantLockTest3.testBaseMethod  avgt   10  0.003 ±  0.001  us/op
 * ReentrantLockTest3.testLockMethod  avgt   10  0.023 ±  0.001  us/op
 * ReentrantLockTest3.testSyncMethod  avgt   10  0.005 ±  0.001  us/op
 * 在单线程访问的情况下，synchronized关键字的性能要远远高于lock锁，
 * 这主要得益于JDK内部对于synchronized关键字的不断优化升级，另外在单线程的情况下，
 * synchronized关键字的jvm指令在运行期间也会被优化。
 */
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
// 单线程
@Threads(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class ReentrantLockTest3 {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(ReentrantLockTest3.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }

    private Test test;

    /**
     * 每一个批次都会产生一个新的test实例
     */
    @Setup(Level.Iteration)
    public void setUp() {
        test = new Test();
    }

    @Benchmark
    public void testBaseMethod(Blackhole blackhole) {
        blackhole.consume(test.baseMethod());
    }

    @Benchmark
    public void testLockMethod(Blackhole blackhole) {
        blackhole.consume(test.lockMethod());
    }

    @Benchmark
    public void testSyncMethod(Blackhole blackhole) {
        blackhole.consume(test.syncMethod());
    }

    public static class Test {
        private int x = 10;
        private final Lock lock = new ReentrantLock();

        public int baseMethod() {
            return x;
        }

        public int lockMethod() {
            lock.lock();
            try {
                return x;
            } finally {
                lock.unlock();
            }
        }

        public int syncMethod() {
            synchronized (this) {
                return x;
            }
        }
    }
}
