package com.chenjj.concurrent.utils;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * 5个读线程、5个写线程
 * Benchmark                                                Mode  Cnt          Score          Error  Units
 * StampedLockTest4.lock                                   thrpt   20   32294814.498 ±   270737.894  ops/s
 * StampedLockTest4.lock:lockGet                           thrpt   20   15175098.845 ±   289246.939  ops/s
 * StampedLockTest4.lock:lockInc                           thrpt   20   17119715.653 ±   235443.608  ops/s
 * StampedLockTest4.rwlock                                 thrpt   20    9131645.445 ±  2182814.113  ops/s
 * StampedLockTest4.rwlock:writeLockGet                    thrpt   20    6373040.267 ±   279502.751  ops/s
 * StampedLockTest4.rwlock:writeLockInc                    thrpt   20    2758605.178 ±  2304946.361  ops/s
 * StampedLockTest4.stampedLock                            thrpt   20   26794587.590 ±   645609.831  ops/s
 * StampedLockTest4.stampedLock:stampedLockInc             thrpt   20   26741899.964 ±   644283.800  ops/s
 * StampedLockTest4.stampedLock:stampedReadLockGet         thrpt   20      52687.626 ±     1403.729  ops/s
 * StampedLockTest4.stampedOptimistic                      thrpt   20  378368852.438 ± 15320238.162  ops/s
 * StampedLockTest4.stampedOptimistic:stampedLockInc2      thrpt   20    2728172.800 ±   561872.144  ops/s
 * StampedLockTest4.stampedOptimistic:stampedReadLockGet2  thrpt   20  375640679.638 ± 15674532.230  ops/s
 * 总体性能：optimistic>lock>stampedLock>rwlock
 * 读性能： optimistic>lock>rwlock>stampedLock
 * 写性能： stampedLock>lock>rwlock>optimistic
 * 10个读10个写线程
 * ......
 * 16个读4个写线程
 * ......
 * 19个读1个写线程
 * ......
 */
// 吞吐量，即每秒方法的调用次数
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 20)
@Measurement(iterations = 20)
@OutputTimeUnit(TimeUnit.SECONDS)
public class StampedLockTest4 {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(StampedLockTest4.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }

    @GroupThreads(5)
    @Group("lock")
    @Benchmark
    public void lockInc(Test test) {
        test.lockInc();
    }

    @GroupThreads(5)
    @Group("lock")
    @Benchmark
    public void lockGet(Test test, Blackhole blackhole) {
        blackhole.consume(test.lockGet());
    }

    @GroupThreads(5)
    @Group("rwlock")
    @Benchmark
    public void writeLockInc(Test test) {
        test.writeLockInc();
    }

    @GroupThreads(5)
    @Group("rwlock")
    @Benchmark
    public void writeLockGet(Test test, Blackhole blackhole) {
        blackhole.consume(test.readLockGet());
    }

    @GroupThreads(5)
    @Group("stampedLock")
    @Benchmark
    public void stampedLockInc(Test test) {
        test.stampedLockInc();
    }

    @GroupThreads(5)
    @Group("stampedLock")
    @Benchmark
    public void stampedReadLockGet(Test test, Blackhole blackhole) {
        blackhole.consume(test.stampedReadLockGet());
    }

    @GroupThreads(5)
    @Group("stampedOptimistic")
    @Benchmark
    public void stampedLockInc2(Test test) {
        test.stampedLockInc();
    }

    @GroupThreads(5)
    @Group("stampedOptimistic")
    @Benchmark
    public void stampedReadLockGet2(Test test, Blackhole blackhole) {
        blackhole.consume(test.stampedOptimisticReadLockGet());
    }

    @State(Scope.Group)
    public static class Test {
        private int x = 10;
        private final Lock lock = new ReentrantLock();
        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Lock readLock = readWriteLock.readLock();
        private final Lock writeLock = readWriteLock.writeLock();
        private final StampedLock stampedLock = new StampedLock();

        public void stampedLockInc() {
            long stamped = stampedLock.writeLock();
            try {
                x++;
            } finally {
                stampedLock.unlockWrite(stamped);
            }
        }

        public int stampedReadLockGet() {
            long stamped = stampedLock.readLock();
            try {
                return x;
            } finally {
                stampedLock.unlockRead(stamped);
            }
        }

        public int stampedOptimisticReadLockGet() {
            long stamped = stampedLock.tryOptimisticRead();
            if (!stampedLock.validate(stamped)) {
                stamped = stampedLock.readLock();
                try {
                    return x;
                } finally {
                    stampedLock.unlockRead(stamped);
                }
            }
            return x;
        }

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
