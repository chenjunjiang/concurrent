package com.chenjj.concurrent.utils;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁性能比较-10个线程读10个线程写的性能比较
 * Benchmark                                               Mode  Cnt  Score   Error  Units
 * ReentrantReadWriteLockTest3.lock                        avgt   10  0.675 ± 0.016  us/op
 * ReentrantReadWriteLockTest3.lock:lockGet                avgt   10  0.954 ± 0.040  us/op
 * ReentrantReadWriteLockTest3.lock:lockInc                avgt   10  0.397 ± 0.012  us/op
 * ReentrantReadWriteLockTest3.readWriteLock               avgt   10  1.274 ± 0.147  us/op
 * ReentrantReadWriteLockTest3.readWriteLock:readLockGet   avgt   10  1.622 ± 0.165  us/op
 * ReentrantReadWriteLockTest3.readWriteLock:writeLockInc  avgt   10  0.926 ± 0.350  us/op
 * ReentrantReadWriteLockTest3.sync                        avgt   10  0.796 ± 0.058  us/op
 * ReentrantReadWriteLockTest3.sync:syncGet                avgt   10  0.915 ± 0.076  us/op
 * ReentrantReadWriteLockTest3.sync:syncInc                avgt   10  0.676 ± 0.043  us/op
 * 基准测试的结果显而易见，仍旧是读写锁的表现最差。
 */
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ReentrantReadWriteLockTest3 {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(ReentrantReadWriteLockTest3.class.getSimpleName())
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

    @GroupThreads(10)
    @Group("readWriteLock")
    @Benchmark
    public void writeLockInc(Test test) {
        test.writeLockInc();
    }

    @GroupThreads(10)
    @Group("readWriteLock")
    @Benchmark
    public void readLockGet(Test test, Blackhole blackhole) {
        blackhole.consume(test.readLockGet());
    }

    @State(Scope.Group)
    public static class Test {
        private int x = 10;
        private final Lock lock = new ReentrantLock();
        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Lock readLock = readWriteLock.readLock();
        private final Lock writeLock = readWriteLock.writeLock();

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

        public void writeLockInc() {
            writeLock.lock();
            try {
                x++;
            } finally {
                writeLock.unlock();
            }
        }

        public int readLockGet() {
            readLock.lock();
            try {
                return x;
            } finally {
                readLock.unlock();
            }
        }
    }
}
