package com.chenjj.concurrent.atomic;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Benchmark                                        Mode  Cnt  Score   Error  Units
 * SynchronizedVsLockVsAtomicInteger.atomic         avgt   10  0.259 ± 0.008  us/op
 * SynchronizedVsLockVsAtomicInteger.atomic:·stack  avgt         NaN            ---
 * SynchronizedVsLockVsAtomicInteger.lock           avgt   10  0.298 ± 0.021  us/op
 * SynchronizedVsLockVsAtomicInteger.lock:·stack    avgt         NaN            ---
 * SynchronizedVsLockVsAtomicInteger.sync           avgt   10  0.382 ± 0.031  us/op
 * SynchronizedVsLockVsAtomicInteger.sync:·stack    avgt         NaN            ---
 * 从基准测试的结果不难看出，AtomicInteger的表现更优，在该基准测试的配置中，我们增加了StackProfiler，因此很容易窥探出AtomicInteger表现优异的原因。
 * atomic:
 *  99.8%         RUNNABLE
 *   0.2%         WAITING
 * lock:
 *  78.7%         WAITING
 *  21.3%         RUNNABLE
 * sync:
 *  78.5%         BLOCKED
 *  21.5%         RUNNABLE
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SynchronizedVsLockVsAtomicInteger {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(SynchronizedVsLockVsAtomicInteger.class.getSimpleName())
                .forks(1)
                .timeout(TimeValue.seconds(10))
                .addProfiler(StackProfiler.class)
                .build();
        new Runner(options).run();
    }

    @State(Scope.Group)
    public static class IntMonitor {
        private int x;
        private final Lock lock = new ReentrantLock();

        /**
         * 使用显示锁Lock进行共享资源同步
         */
        public void lockInc() {
            lock.lock();
            try {
                x++;
            } finally {
                lock.unlock();
            }
        }

        /**
         * 使用synchronized关键字进行共享资源同步
         */
        public void synInc() {
            synchronized (this) {
                x++;
            }
        }
    }

    /**
     * 使用Atomic进行共享资源同步
     */
    @State(Scope.Group)
    public static class AtomicIntegerMonitor {
        private AtomicInteger x = new AtomicInteger();

        public void inc() {
            x.incrementAndGet();
        }
    }

    @GroupThreads(10)
    @Group("sync")
    @Benchmark
    public void syncInc(IntMonitor monitor) {
        monitor.synInc();
    }

    @GroupThreads(10)
    @Group("lock")
    @Benchmark
    public void lockInc(IntMonitor monitor) {
        monitor.lockInc();
    }

    @GroupThreads(10)
    @Group("atomic")
    @Benchmark
    public void atomicInc(AtomicIntegerMonitor monitor) {
        monitor.inc();
    }
}
