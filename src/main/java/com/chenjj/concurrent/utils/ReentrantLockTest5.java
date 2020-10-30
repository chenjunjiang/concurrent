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
 * 多线程读写操作性能对比
 * Benchmark                        Mode  Cnt  Score   Error  Units
 * ReentrantLockTest5.lock          avgt   10  0.631 ± 0.011  us/op
 * ReentrantLockTest5.lock:lockGet  avgt   10  0.839 ± 0.020  us/op
 * ReentrantLockTest5.lock:lockInc  avgt   10  0.423 ± 0.013  us/op
 * ReentrantLockTest5.sync          avgt   10  0.782 ± 0.039  us/op
 * ReentrantLockTest5.sync:syncGet  avgt   10  0.913 ± 0.064  us/op
 * ReentrantLockTest5.sync:syncInc  avgt   10  0.651 ± 0.032  us/op
 */
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ReentrantLockTest5 {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(ReentrantLockTest5.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }

    @GroupThreads(10)
    @Group("lock")
    @Benchmark
    public void lockInc(Test test) {
        test.lockInc();
    }

    @GroupThreads(10)
    @Group("lock")
    @Benchmark
    public void lockGet(Test test, Blackhole blackhole) {
        blackhole.consume(test.lockGet());
    }

    @GroupThreads(10)
    @Group("sync")
    @Benchmark
    public void syncInc(Test test) {
        test.syncInc();
    }

    @GroupThreads(10)
    @Group("sync")
    @Benchmark
    public void syncGet(Test test, Blackhole blackhole) {
        blackhole.consume(test.syncGet());
    }

    @State(Scope.Group)
    public static class Test {
        private int x = 10;
        private final Lock lock = new ReentrantLock();

        public void lockInc() {
            lock.lock();
            try {
                x++;
            } finally {
                lock.unlock();
            }
        }

        public int lockGet() {
            lock.lock();
            try {
                return x;
            } finally {
                lock.unlock();
            }
        }

        public void syncInc() {
            synchronized (this) {
                x++;
            }
        }

        public int syncGet() {
            synchronized (this) {
                return x;
            }
        }
    }
}
