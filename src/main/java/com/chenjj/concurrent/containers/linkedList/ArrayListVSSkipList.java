package com.chenjj.concurrent.containers.linkedList;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark                                                 Mode  Cnt  Score   Error  Units
 * linkedList.ArrayListVSSkipList.binarySearchFromArrayList  avgt   20  0.445 ± 0.007  us/op
 * linkedList.ArrayListVSSkipList.searchFromSkipList         avgt   20  0.845 ± 0.004  us/op
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 20)
@Measurement(iterations = 20)
@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class ArrayListVSSkipList {
    private ArrayList<Integer> arrayList;
    private SkipList skipList;
    private Random random;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(ArrayListVSSkipList.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    @Setup(Level.Trial)
    public void setUp() {
        this.random = new Random(System.currentTimeMillis());
        this.arrayList = new ArrayList<>();
        this.skipList = new SkipList();
        for (int i = 0; i < 500_000; i++) {
            arrayList.add(i);
            skipList.add(i);
        }
    }

    @Benchmark
    public void binarySearchFromArrayList(Blackhole blackhole) {
        int randomValue = random.nextInt(500_000);
        int result = Collections.binarySearch(arrayList, randomValue);
        blackhole.consume(result);
    }

    @Benchmark
    public void searchFromSkipList(Blackhole blackhole) {
        int randomValue = random.nextInt(500_000);
        Object result = this.skipList.search(randomValue);
        blackhole.consume(result);
    }
}
