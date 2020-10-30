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
 * 读写锁性能比较-10个线程的只读性能比较
 * Benchmark                             Mode  Cnt  Score    Error  Units
 * ReentrantReadWriteLockTest2.base      avgt   10  0.012 ±  0.001  us/op
 * ReentrantReadWriteLockTest2.lock      avgt   10  0.324 ±  0.003  us/op
 * ReentrantReadWriteLockTest2.readLock  avgt   10  1.548 ±  0.023  us/op
 * ReentrantReadWriteLockTest2.sync      avgt   10  0.403 ±  0.005  us/op
 * 根据基准测试的结果来看，在没有任何写操作的情况下，读锁的效率反倒是最差的，这的确令人感到失望和惊讶，
 * 实际上，ReadWriteLock的性能表现确实不尽如人意，这也是在JDK1.8版本中引入StampedLock的原因之一。
 */
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ReentrantReadWriteLockTest2 {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(ReentrantReadWriteLockTest2.class.getSimpleName())
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

    @GroupThreads(10)
    @Group("readLock")
    @Benchmark
    public void testReadLockMethod(Test test, Blackhole blackhole) {
        blackhole.consume(test.readLockMethod());
    }

    @State(Scope.Group)
    public static class Test {
        private int x = 10;
        private final Lock lock = new ReentrantLock();
        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Lock readLock = readWriteLock.readLock();

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

        public int readLockMethod() {
            readLock.lock();
            try {
                return x;
            } finally {
                readLock.unlock();
            }
        }
    }
}
