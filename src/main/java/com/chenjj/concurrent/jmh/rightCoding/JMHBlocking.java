package com.chenjj.concurrent.jmh.rightCoding;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 我们针对BlockingQueue同时进行读（take）和写（put）的操作，但是很遗憾，
 * 在某些情况下（或许是第一次运行时）程序会出现长时间的阻塞，对于每一批次的Measurement，
 * 当然也包括Warmup中，put和take方法都会同时被多线程执行。想象一下，假设put方法最先执行结束，
 * take方法无法再次从blocking queue中获取元素的时候将会一直阻塞下去，
 * 同样，take方法最先执行结束后，put方法在放满10个元素后再也无法存入新的元素，进而进入了阻塞状态，
 * 这两种情况都会等到每一次iteration（批次）超时（默认是10分钟）后才能继续往下执行。
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Group)
public class JMHBlocking {
    private BlockingQueue<Integer> queue;

    private final static int VALUE = Integer.MAX_VALUE;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHBlocking.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Setup
    public void init() {
        this.queue = new ArrayBlockingQueue<>(10);
    }

    @GroupThreads(5)
    @Group("blockingQueue")
    @Benchmark
    public void put() throws InterruptedException {
        this.queue.put(VALUE);
    }

    @GroupThreads(5)
    @Group("blockingQueue")
    @Benchmark
    public int take() throws InterruptedException {
        return this.queue.take();
    }
}
