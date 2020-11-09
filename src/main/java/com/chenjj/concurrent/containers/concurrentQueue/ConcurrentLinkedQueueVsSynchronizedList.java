package com.chenjj.concurrent.containers.concurrentQueue;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                                                                                    Mode  Cnt  Score   Error  Units
 * concurrentQueue.ConcurrentLinkedQueueVsSynchronizedList.concurrent                           avgt   10  0.293 ± 0.036  us/op
 * concurrentQueue.ConcurrentLinkedQueueVsSynchronizedList.concurrent:concurrentLinkedQueueAdd  avgt   10  0.479 ± 0.066  us/op
 * concurrentQueue.ConcurrentLinkedQueueVsSynchronizedList.concurrent:concurrentLinkedQueueGet  avgt   10  0.106 ± 0.082  us/op
 * concurrentQueue.ConcurrentLinkedQueueVsSynchronizedList.sync                                 avgt   10  0.858 ± 0.046  us/op
 * concurrentQueue.ConcurrentLinkedQueueVsSynchronizedList.sync:synchronizedListAdd             avgt   10  0.553 ± 0.019  us/op
 * concurrentQueue.ConcurrentLinkedQueueVsSynchronizedList.sync:synchronizedListGet             avgt   10  1.162 ± 0.090  us/op
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Group)
public class ConcurrentLinkedQueueVsSynchronizedList {
    private SynchronizedLinkedList synchronizedLinkedList;
    private ConcurrentLinkedQueue<String> concurrentLinkedQueue;
    private final String DATA = "TEST";
    private final static Object LOCK = new Object();

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(ConcurrentLinkedQueueVsSynchronizedList.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Setup(Level.Iteration)
    public void setUp() {
        synchronizedLinkedList = new SynchronizedLinkedList();
        concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    }

    @Group("sync")
    @GroupThreads(5)
    @Benchmark
    public void synchronizedListAdd() {
        synchronizedLinkedList.addLast(DATA);
    }

    @Group("sync")
    @GroupThreads(5)
    @Benchmark
    public void synchronizedListGet() {
        synchronizedLinkedList.removeFirst();
    }

    @Group("concurrent")
    @GroupThreads(5)
    @Benchmark
    public void concurrentLinkedQueueAdd() {
        concurrentLinkedQueue.offer(DATA);
    }

    @Group("concurrent")
    @GroupThreads(5)
    @Benchmark
    public void concurrentLinkedQueueGet() {
        concurrentLinkedQueue.poll();
    }

    private static class SynchronizedLinkedList {
        private LinkedList<String> list = new LinkedList<>();

        void addLast(String element) {
            synchronized (LOCK) {
                list.addLast(element);
            }
        }

        String removeFirst() {
            synchronized (LOCK) {
                if (list.isEmpty()) {
                    return null;
                }
                return list.removeFirst();
            }
        }
    }
}
