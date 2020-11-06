package com.chenjj.concurrent.containers.linkedList;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 对比ArrayList和LinkedList在二分查找算法下的性能表现
 * Benchmark                                                            Mode  Cnt      Score      Error  Units
 * linkedList.BinarySearchArrayVSLinkedList.binarySearchFromArrayList   avgt   20      1.167 ±    0.042  us/op
 * linkedList.BinarySearchArrayVSLinkedList.binarySearchFromLinkedList  avgt   20  81933.293 ± 4065.717  us/op
 * 差距非常大
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 20)
@Measurement(iterations = 20)
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class BinarySearchArrayVSLinkedList {
    private ArrayList<Integer> arrayList;
    private LinkedList<Integer> linkedList;
    private Random random;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(BinarySearchArrayVSLinkedList.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    /**
     * 每一个基准测试方法的所有批次执行的前被执行
     */
    @Setup(Level.Trial)
    public void setUp() {
        this.random = new Random(System.currentTimeMillis());
        this.arrayList = new ArrayList<>();
        this.linkedList = new LinkedList<>();
        for (int i = 0; i < 10_000_000; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
    }

    @Benchmark
    public void binarySearchFromArrayList(Blackhole blackhole) {
        int randomValue = random.nextInt(10_000_000);
        int result = Collections.binarySearch(arrayList, randomValue);
        blackhole.consume(result);
    }

    @Benchmark
    public void binarySearchFromLinkedList(Blackhole blackhole) {
        int randomValue = random.nextInt(10_000_000);
        int result = Collections.binarySearch(linkedList, randomValue);
        blackhole.consume(result);
    }
}
