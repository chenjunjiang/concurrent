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
 * 多线程读操作性能对比
 * Benchmark                Mode  Cnt  Score   Error  Units
 * ReentrantLockTest4.base  avgt   10  0.013 ± 0.002  us/op
 * ReentrantLockTest4.lock  avgt   10  0.327 ± 0.008  us/op
 * ReentrantLockTest4.sync  avgt   10  0.451 ± 0.041  us/op
 * 执行基准测试会发现在10个线程的情况下，显式锁Lock的性能要优于synchronized关键字。
 */
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ReentrantLockTest4 {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(ReentrantLockTest4.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }

    @GroupThreads(10)
    @Group("base")
    @Benchmark
    public void testBaseMethod(Test test, Blackhole blackhole) {
        blackhole.consume(test.baseMethod());
    }

    @GroupThreads(10)
    @Group("lock")
    @Benchmark
    public void testLockMethod(Test test, Blackhole blackhole) {
        blackhole.consume(test.lockMethod());
    }

    @GroupThreads(10)
    @Group("sync")
    @Benchmark
    public void testSyncMethod(Test test, Blackhole blackhole) {
        blackhole.consume(test.syncMethod());
    }

    @State(Scope.Group)
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
